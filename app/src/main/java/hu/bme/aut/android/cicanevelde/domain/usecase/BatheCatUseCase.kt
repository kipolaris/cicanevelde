package hu.bme.aut.android.cicanevelde.domain.usecase

import hu.bme.aut.android.cicanevelde.data.repository.CatRepository
import hu.bme.aut.android.cicanevelde.data.repository.GameStateRepository
import hu.bme.aut.android.cicanevelde.data.repository.ItemRepository
import hu.bme.aut.android.cicanevelde.domain.model.Cat
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.result.CareActionResult
import hu.bme.aut.android.cicanevelde.domain.result.item.RemoveItemResult
import javax.inject.Inject

class BatheCatUseCase @Inject constructor(
    private val catRepository: CatRepository,
    private val gameStateRepository: GameStateRepository,
    private val itemRepository: ItemRepository
) {
    suspend operator fun invoke(cat: Cat): CareActionResult {
        val refreshedCat = catRepository.refreshCatStats(cat)

        if (refreshedCat.stats.energy < 20) return CareActionResult.NotEnoughEnergy

        return when (itemRepository.removeItem(ItemCode.CAT_SHAMPOO)) {
            RemoveItemResult.Success -> {
                val newHygiene = 100
                val newEnergy = (refreshedCat.stats.energy - 15).coerceIn(0,100)
                val newHappiness = (refreshedCat.stats.happiness - 20).coerceIn(0,100)

                val updatedStats = refreshedCat.stats.copy(
                    energy = newEnergy,
                    hygiene = newHygiene,
                    happiness = newHappiness,
                    lastUpdated = System.currentTimeMillis()
                )

                catRepository.updateCatStats(refreshedCat, updatedStats)

                gameStateRepository.addCatCoins(25)

                CareActionResult.Success(25)
            }
            RemoveItemResult.ItemNotOwned, RemoveItemResult.InvalidAmount -> CareActionResult.NoItemAvailable
        }
    }
}