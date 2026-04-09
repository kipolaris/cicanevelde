package hu.bme.aut.android.cicanevelde.domain.usecase

import hu.bme.aut.android.cicanevelde.data.entity.CatEntity
import hu.bme.aut.android.cicanevelde.data.repository.CatRepository
import hu.bme.aut.android.cicanevelde.data.repository.GameStateRepository
import hu.bme.aut.android.cicanevelde.data.repository.ItemRepository
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.result.CareActionResult
import hu.bme.aut.android.cicanevelde.domain.result.item.RemoveItemResult
import javax.inject.Inject

class GiveCatTreatUseCase @Inject constructor(
    private val catRepository: CatRepository,
    private val itemRepository: ItemRepository,
    private val gameStateRepository: GameStateRepository
) {
    suspend operator fun invoke(cat: CatEntity): CareActionResult {
        val refreshedCat = catRepository.refreshCatStats(cat)

        return when (itemRepository.removeItem(ItemCode.TREATS)) {
            RemoveItemResult.Success -> {
                val newHunger = (refreshedCat.stats.hunger + 10).coerceIn(0,100)
                val newHappiness = (refreshedCat.stats.happiness + 5).coerceIn(0,100)

                val updatedStats = refreshedCat.stats.copy(
                    hunger = newHunger,
                    happiness = newHappiness,
                    lastUpdated = System.currentTimeMillis()
                )

                catRepository.updateCatStats(refreshedCat,updatedStats)

                gameStateRepository.addCatCoins(10)

                CareActionResult.Success(10)
            }
            RemoveItemResult.ItemNotOwned, RemoveItemResult.InvalidAmount -> CareActionResult.NoItemAvailable
        }
    }
}