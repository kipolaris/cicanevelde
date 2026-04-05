package hu.bme.aut.android.cicanevelde.repository

import hu.bme.aut.android.cicanevelde.data.dao.CatDao
import hu.bme.aut.android.cicanevelde.data.entity.CatEntity
import hu.bme.aut.android.cicanevelde.domain.model.Stats
import hu.bme.aut.android.cicanevelde.domain.model.enums.Gender
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.model.enums.Pattern
import hu.bme.aut.android.cicanevelde.domain.result.CareActionResult
import hu.bme.aut.android.cicanevelde.domain.result.EatFoodResult
import hu.bme.aut.android.cicanevelde.domain.result.SleepResult
import hu.bme.aut.android.cicanevelde.domain.result.item.EmptyBowlResult
import hu.bme.aut.android.cicanevelde.domain.result.item.FillLitterResult
import hu.bme.aut.android.cicanevelde.domain.result.item.RemoveItemResult
import hu.bme.aut.android.cicanevelde.domain.result.UseLitterResult
import kotlinx.coroutines.flow.Flow

class CatRepository(
    private val catDao: CatDao,
    private val itemRepository: ItemRepository,
    private val bowlRepository: BowlRepository,
    private val litterRepository: LitterRepository,
    private val gameStateRepository: GameStateRepository
) {
    fun getAllCats(): Flow<List<CatEntity>> = catDao.getAllCats()

    suspend fun getCatById(catId: Long): CatEntity? = catDao.getCatById(catId)

    suspend fun createCat(name: String, gender: Gender, pattern: Pattern) {
        catDao.insertCat(
            CatEntity(
                name = name,
                gender = gender,
                pattern = pattern,
                stats = Stats(100,100,100,100,100,System.currentTimeMillis())
            )
        )
    }

    private suspend fun updateCat(cat: CatEntity) {
        catDao.updateCat(cat)
    }

    private fun clampStats(value: Int): Int {
        return value.coerceIn(0,100)
    }

    private suspend fun saveUpdatedStats(cat: CatEntity, updatedStats: Stats): CatEntity {
        val updatedCat = cat.copy(stats = updatedStats)
        updateCat(updatedCat)
        return updatedCat
    }

    suspend fun refreshCatStats(cat: CatEntity): CatEntity {
        val now = System.currentTimeMillis()
        val elapsedMillis = now - cat.stats.lastUpdated

        if (elapsedMillis <= 0) return cat

        val elapsedMinutes = elapsedMillis / (1000 * 60)

        val hungerLoss = (elapsedMinutes / 30).toInt()
        val hygieneLoss = (elapsedMinutes / 60).toInt()
        val happinessLoss = (elapsedMinutes / 45).toInt()
        val bladderLoss = (elapsedMinutes / 60).toInt()
        val energyGain = (elapsedMinutes / 20).toInt()

        val updatedStats = cat.stats.copy(
            hunger = clampStats(cat.stats.hunger - hungerLoss),
            happiness = clampStats(cat.stats.happiness - happinessLoss),
            hygiene = clampStats(cat.stats.hygiene - hygieneLoss),
            energy = clampStats(cat.stats.energy + energyGain),
            bladder = clampStats(cat.stats.bladder - bladderLoss),
            lastUpdated = now
        )

        val updatedCat = cat.copy(stats = updatedStats)

        updateCat(updatedCat)

        return updatedCat
    }

    suspend fun petCat(cat: CatEntity): CareActionResult {
        val refreshedCat = refreshCatStats(cat)
        val newHappiness = clampStats(refreshedCat.stats.happiness + 10)
        val updatedStats = refreshedCat.stats.copy(
            happiness = newHappiness,
            lastUpdated = System.currentTimeMillis()
        )

        saveUpdatedStats(refreshedCat, updatedStats)

        gameStateRepository.addCatCoins(10)

        return CareActionResult.Success(10)
    }

    suspend fun brushCat(cat: CatEntity): CareActionResult {
        val refreshedCat = refreshCatStats(cat)

        itemRepository.getOwnedItemByCode(ItemCode.BRUSH) ?: return CareActionResult.NoItemAvailable

        val newHygiene = clampStats(refreshedCat.stats.hygiene + 10)
        val newHappiness = clampStats(refreshedCat.stats.happiness + 5)

        val updatedStats = refreshedCat.stats.copy(
            hygiene = newHygiene,
            happiness = newHappiness,
            lastUpdated = System.currentTimeMillis()
        )

        saveUpdatedStats(refreshedCat, updatedStats)

        gameStateRepository.addCatCoins(15)

        return CareActionResult.Success(15)
    }

    suspend fun batheCat(cat: CatEntity): CareActionResult {
        val refreshedCat = refreshCatStats(cat)

        if (refreshedCat.stats.energy < 25) return CareActionResult.NotEnoughEnergy

        return when (itemRepository.removeItem(ItemCode.CAT_SHAMPOO)) {
            RemoveItemResult.Success -> {
                val newHygiene = 100
                val newEnergy = clampStats(refreshedCat.stats.energy - 15)
                val newHappiness = clampStats(refreshedCat.stats.happiness - 20)

                val updatedStats = refreshedCat.stats.copy(
                    energy = newEnergy,
                    hygiene = newHygiene,
                    happiness = newHappiness,
                    lastUpdated = System.currentTimeMillis()
                )

                saveUpdatedStats(refreshedCat, updatedStats)

                gameStateRepository.addCatCoins(25)

                CareActionResult.Success(25)
            }
            RemoveItemResult.ItemNotOwned, RemoveItemResult.InvalidAmount -> CareActionResult.NoItemAvailable
        }
    }

    suspend fun playWithCat(cat: CatEntity): CareActionResult {
        val refreshedCat = refreshCatStats(cat)

        if (refreshedCat.stats.energy < 25) return CareActionResult.NotEnoughEnergy

        itemRepository.getOwnedItemByCode(ItemCode.WAND) ?: return CareActionResult.NoItemAvailable

        val newHappiness = clampStats(refreshedCat.stats.happiness + 20)
        val newEnergy = clampStats(refreshedCat.stats.energy - 15)

        val updatedStats = refreshedCat.stats.copy(
            energy = newEnergy,
            happiness = newHappiness,
            lastUpdated = System.currentTimeMillis()
        )

        saveUpdatedStats(refreshedCat,updatedStats)

        gameStateRepository.addCatCoins(20)

        return CareActionResult.Success(20)
    }

    suspend fun giveCatTreat(cat: CatEntity): CareActionResult {
        val refreshedCat = refreshCatStats(cat)

        return when (itemRepository.removeItem(ItemCode.TREATS)) {
            RemoveItemResult.Success -> {
                val newHunger = clampStats(refreshedCat.stats.hunger + 10)
                val newHappiness = clampStats(refreshedCat.stats.happiness + 5)

                val updatedStats = refreshedCat.stats.copy(
                    hunger = newHunger,
                    happiness = newHappiness,
                    lastUpdated = System.currentTimeMillis()
                )

                saveUpdatedStats(refreshedCat,updatedStats)

                gameStateRepository.addCatCoins(10)

                CareActionResult.Success(10)
            }
            RemoveItemResult.ItemNotOwned, RemoveItemResult.InvalidAmount -> CareActionResult.NoItemAvailable
        }
    }

    suspend fun eatFood(cat: CatEntity): EatFoodResult {
        val refreshedCat = refreshCatStats(cat)

        if (refreshedCat.stats.hunger > 70) return EatFoodResult.NotHungryEnough

        val bowl = bowlRepository.getFirstFilledBowl() ?: return EatFoodResult.NoFoodAvailable

        return when (bowlRepository.emptyBowl(bowl.placedItemId)) {
            EmptyBowlResult.Success -> {
                val newHunger = 100
                val newBladder = clampStats(refreshedCat.stats.bladder - 5)

                val updatedStats = refreshedCat.stats.copy(
                    hunger = newHunger,
                    bladder = newBladder,
                    lastUpdated = System.currentTimeMillis()
                )

                saveUpdatedStats(refreshedCat,updatedStats)

                EatFoodResult.Success
            }
            EmptyBowlResult.BowlNotFilled, EmptyBowlResult.BowlNotFound -> EatFoodResult.NoFoodAvailable
        }
    }

    suspend fun useLitterBox(cat: CatEntity): UseLitterResult {
        val refreshedCat = refreshCatStats(cat)

        if (refreshedCat.stats.bladder > 70) return UseLitterResult.NoNeedTo

        val litterBox = litterRepository.getFirstCleanLitter() ?: return UseLitterResult.NoLitterAvailable

        return when (litterRepository.fillLitter(litterBox.id)) {
            FillLitterResult.Success -> {
                val newBladder = 100
                val newHunger = clampStats(refreshedCat.stats.hunger - 10)

                val updatedStats = refreshedCat.stats.copy(
                    hunger = newHunger,
                    bladder = newBladder,
                    lastUpdated = System.currentTimeMillis()
                )

                saveUpdatedStats(refreshedCat, updatedStats)

                UseLitterResult.Success
            }
            FillLitterResult.LitterAlreadyFull, FillLitterResult.LitterBoxNotFound -> UseLitterResult.NoLitterAvailable
        }
    }

    suspend fun sleep(cat: CatEntity): SleepResult {
        val refreshedCat = refreshCatStats(cat)

        if (refreshedCat.stats.energy > 25) return SleepResult.NotTiredEnough

        val newEnergy = 100

        val updatedStats = refreshedCat.stats.copy(
            energy = newEnergy,
            lastUpdated = System.currentTimeMillis()
        )

        saveUpdatedStats(refreshedCat, updatedStats)

        return SleepResult.Success
    }

    suspend fun playWithToy(cat: CatEntity): CareActionResult {
        val refreshedCat = refreshCatStats(cat)

        if (refreshedCat.stats.energy < 25) return CareActionResult.NotEnoughEnergy

        val mouse = itemRepository.getOwnedItemByCode(ItemCode.MOUSE)
        val ball = itemRepository.getOwnedItemByCode(ItemCode.BALL)

        if (mouse == null && ball == null) return CareActionResult.NoItemAvailable

        val newHappiness = clampStats(refreshedCat.stats.happiness + 10)
        val newEnergy = clampStats(refreshedCat.stats.energy - 10)

        val updatedStats = refreshedCat.stats.copy(
            happiness = newHappiness,
            energy = newEnergy,
            lastUpdated = System.currentTimeMillis()
        )

        saveUpdatedStats(refreshedCat, updatedStats)

        return CareActionResult.Success()
    }
}