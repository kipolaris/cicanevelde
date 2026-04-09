package hu.bme.aut.android.cicanevelde.domain.usecase

import hu.bme.aut.android.cicanevelde.data.entity.CatEntity
import hu.bme.aut.android.cicanevelde.data.repository.BowlRepository
import hu.bme.aut.android.cicanevelde.data.repository.CatRepository
import hu.bme.aut.android.cicanevelde.domain.result.EatFoodResult
import hu.bme.aut.android.cicanevelde.domain.result.item.EmptyBowlResult
import javax.inject.Inject

class EatFoodUseCase @Inject constructor(
    private val catRepository: CatRepository,
    private val bowlRepository: BowlRepository
){
    suspend operator fun invoke(cat: CatEntity): EatFoodResult {
        val refreshedCat = catRepository.refreshCatStats(cat)

        if (refreshedCat.stats.hunger > 70) return EatFoodResult.NotHungryEnough

        val bowl = bowlRepository.getFirstFilledBowl() ?: return EatFoodResult.NoFoodAvailable

        return when (bowlRepository.emptyBowl(bowl.placedItemId)) {
            EmptyBowlResult.Success -> {
                val updatedStats = refreshedCat.stats.copy(
                    hunger = 100,
                    bladder = (refreshedCat.stats.bladder - 10).coerceIn(0,100),
                    lastUpdated = System.currentTimeMillis()
                )

                catRepository.updateCatStats(refreshedCat, updatedStats)

                EatFoodResult.Success
            }
            EmptyBowlResult.BowlNotFilled, EmptyBowlResult.BowlNotFound -> EatFoodResult.NoFoodAvailable
        }
    }
}