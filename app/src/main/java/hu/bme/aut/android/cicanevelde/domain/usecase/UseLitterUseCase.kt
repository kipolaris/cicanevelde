package hu.bme.aut.android.cicanevelde.domain.usecase

import hu.bme.aut.android.cicanevelde.data.repository.CatRepository
import hu.bme.aut.android.cicanevelde.data.repository.LitterRepository
import hu.bme.aut.android.cicanevelde.domain.model.Cat
import hu.bme.aut.android.cicanevelde.domain.result.UseLitterResult
import hu.bme.aut.android.cicanevelde.domain.result.item.FillLitterResult
import javax.inject.Inject

class UseLitterUseCase @Inject constructor(
    private val catRepository: CatRepository,
    private val litterRepository: LitterRepository
){
    suspend operator fun invoke(cat: Cat): UseLitterResult {
        val refreshedCat = catRepository.refreshCatStats(cat)

        if (refreshedCat.stats.bladder > 70) return UseLitterResult.NoNeedTo

        val litterBox = litterRepository.getFirstCleanLitter() ?: return UseLitterResult.NoLitterAvailable

        return when (litterRepository.fillLitter(litterBox.id)) {
            FillLitterResult.Success -> {
                val newBladder = 100
                val newHunger = (refreshedCat.stats.hunger -10).coerceIn(0, 100)

                val updatedStats = refreshedCat.stats.copy(
                    hunger = newHunger,
                    bladder = newBladder,
                    lastUpdated = System.currentTimeMillis()
                )

                catRepository.updateCatStats(refreshedCat, updatedStats)

                UseLitterResult.Success
            }
            FillLitterResult.LitterAlreadyFull, FillLitterResult.LitterBoxNotFound -> UseLitterResult.NoLitterAvailable
        }
    }
}