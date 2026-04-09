package hu.bme.aut.android.cicanevelde.domain.usecase

import hu.bme.aut.android.cicanevelde.data.entity.CatEntity
import hu.bme.aut.android.cicanevelde.data.repository.CatRepository
import hu.bme.aut.android.cicanevelde.data.repository.GameStateRepository
import hu.bme.aut.android.cicanevelde.domain.result.CareActionResult
import javax.inject.Inject

class PetCatUseCase @Inject constructor(
    private val catRepository: CatRepository,
    private val gameStateRepository: GameStateRepository
) {
    suspend operator fun invoke(cat: CatEntity): CareActionResult {
        val refreshedCat = catRepository.refreshCatStats(cat)

        if (refreshedCat.stats.energy < 20) return CareActionResult.NotEnoughEnergy

        val newHappiness = (refreshedCat.stats.happiness + 10).coerceIn(0,100)
        val updatedStats = refreshedCat.stats.copy(
            happiness = newHappiness,
            lastUpdated = System.currentTimeMillis()
        )

        catRepository.updateCatStats(refreshedCat, updatedStats)

        gameStateRepository.addCatCoins(10)

        return CareActionResult.Success(10)
    }
}