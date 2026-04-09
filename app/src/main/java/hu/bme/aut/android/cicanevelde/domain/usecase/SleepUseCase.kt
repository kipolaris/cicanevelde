package hu.bme.aut.android.cicanevelde.domain.usecase

import hu.bme.aut.android.cicanevelde.data.repository.CatRepository
import hu.bme.aut.android.cicanevelde.domain.model.Cat
import hu.bme.aut.android.cicanevelde.domain.result.SleepResult
import javax.inject.Inject

class SleepUseCase @Inject constructor(
    private val catRepository: CatRepository,
) {
    suspend operator fun invoke(cat: Cat): SleepResult {
        val refreshedCat = catRepository.refreshCatStats(cat)

        if (refreshedCat.stats.energy > 30) return SleepResult.NotTiredEnough

        if (refreshedCat.stats.isSleeping) return SleepResult.Success

        val updatedStats = refreshedCat.stats.copy(
            isSleeping = true,
            lastUpdated = System.currentTimeMillis()
        )

        catRepository.updateCatStats(refreshedCat, updatedStats)

        return SleepResult.Success
    }
}