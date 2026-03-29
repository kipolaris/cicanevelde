package hu.bme.aut.android.cicanevelde.repository

import hu.bme.aut.android.cicanevelde.data.dao.CatDao
import hu.bme.aut.android.cicanevelde.data.entity.CatEntity
import hu.bme.aut.android.cicanevelde.domain.model.Stats
import hu.bme.aut.android.cicanevelde.domain.model.enums.Gender
import hu.bme.aut.android.cicanevelde.domain.model.enums.Pattern
import hu.bme.aut.android.cicanevelde.domain.result.CareActionResult
import kotlinx.coroutines.flow.Flow

class CatRepository(
    private val catDao: CatDao
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

    suspend fun refreshCatStats(cat: CatEntity): CatEntity? {
        val now = System.currentTimeMillis()
        val elapsedMillis = cat.stats.lastUpdated - now

        if (elapsedMillis < 0) return cat

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

    /*suspend fun feedCat(cat: CatEntity): CareActionResult {

    }*/
}