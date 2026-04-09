package hu.bme.aut.android.cicanevelde.data.repository

import hu.bme.aut.android.cicanevelde.data.dao.CatDao
import hu.bme.aut.android.cicanevelde.data.entity.CatEntity
import hu.bme.aut.android.cicanevelde.domain.mappers.toDomain
import hu.bme.aut.android.cicanevelde.domain.mappers.toEntity
import hu.bme.aut.android.cicanevelde.domain.model.Cat
import hu.bme.aut.android.cicanevelde.domain.model.Stats
import hu.bme.aut.android.cicanevelde.domain.model.enums.Gender
import hu.bme.aut.android.cicanevelde.domain.model.enums.Pattern
import hu.bme.aut.android.cicanevelde.domain.result.CareActionResult

class CatRepository(
    private val catDao: CatDao
) {
    suspend fun getAllCats(): List<Cat> {
        val catEntities = catDao.getAllCats()
        val cats = mutableListOf<Cat>()

        catEntities.forEach { cats.add(it.toDomain()) }

        return cats
    }

    private suspend fun getCatCount(): Int = catDao.getCatCount()

    suspend fun hasAnyCat(): Boolean = getCatCount() > 0

    suspend fun getCatById(catId: Long): Cat? = catDao.getCatById(catId)?.toDomain()

    suspend fun createCat(name: String, gender: Gender, pattern: Pattern) {
        catDao.insertCat(
            CatEntity(
                name = name,
                gender = gender,
                pattern = pattern,
                stats = Stats(100,100,100,100,100, false, System.currentTimeMillis())
            )
        )
    }

    private suspend fun updateCat(cat: Cat) {
        catDao.updateCat(cat.toEntity())
    }

    private fun clampStats(value: Int): Int {
        return value.coerceIn(0,100)
    }

    suspend fun updateCatStats(cat: Cat, updatedStats: Stats): Cat {
        val updatedCat = cat.copy(stats = updatedStats)
        updateCat(updatedCat)
        return updatedCat
    }

    suspend fun refreshCatStats(cat: Cat): Cat {
        val now = System.currentTimeMillis()
        val elapsedMillis = now - cat.stats.lastUpdated

        if (elapsedMillis <= 0) return cat

        val elapsedMinutes = elapsedMillis / (1000 * 60)

        val hungerLoss = (elapsedMinutes / 30).toInt()
        val hygieneLoss = (elapsedMinutes / 60).toInt()
        val happinessLoss = (elapsedMinutes / 45).toInt()
        val bladderLoss = (elapsedMinutes / 60).toInt()
        val energyGain = if (cat.stats.isSleeping) {
            (elapsedMinutes / 5).toInt()
        } else {
            (elapsedMinutes / 20).toInt()
        }

        val newEnergy = clampStats(cat.stats.energy + energyGain)
        val shouldStillSleep = cat.stats.isSleeping && newEnergy < 100

        val updatedStats = cat.stats.copy(
            hunger = clampStats(cat.stats.hunger - hungerLoss),
            happiness = clampStats(cat.stats.happiness - happinessLoss),
            hygiene = clampStats(cat.stats.hygiene - hygieneLoss),
            energy = newEnergy,
            bladder = clampStats(cat.stats.bladder - bladderLoss),
            isSleeping = shouldStillSleep,
            lastUpdated = now
        )

        val updatedCat = cat.copy(stats = updatedStats)

        updateCat(updatedCat)

        return updatedCat
    }

    suspend fun wakeUp(cat: Cat): CareActionResult {
        val refreshedCat = refreshCatStats(cat)

        if (!refreshedCat.stats.isSleeping) return CareActionResult.Success()

        if (refreshedCat.stats.energy < 25) return CareActionResult.NotEnoughEnergy

        val updatedStats = refreshedCat.stats.copy(
            isSleeping = false,
            lastUpdated = System.currentTimeMillis()
        )

        updateCatStats(refreshedCat, updatedStats)

        return CareActionResult.Success()
    }
}