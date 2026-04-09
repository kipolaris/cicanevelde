package hu.bme.aut.android.cicanevelde.domain.usecase

import hu.bme.aut.android.cicanevelde.data.repository.CatRepository
import hu.bme.aut.android.cicanevelde.data.repository.GameStateRepository
import hu.bme.aut.android.cicanevelde.data.repository.ItemRepository
import hu.bme.aut.android.cicanevelde.domain.model.Cat
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.result.CareActionResult
import javax.inject.Inject

class BrushCatUseCase @Inject constructor(
    private val catRepository: CatRepository,
    private val itemRepository: ItemRepository,
    private val gameStateRepository: GameStateRepository
) {
    suspend operator fun invoke(cat: Cat): CareActionResult {
        val refreshedCat = catRepository.refreshCatStats(cat)

        if (refreshedCat.stats.energy < 20) return CareActionResult.NotEnoughEnergy

        itemRepository.getOwnedItemByCode(ItemCode.BRUSH) ?: return CareActionResult.NoItemAvailable

        val newHygiene = (refreshedCat.stats.hygiene + 15).coerceIn(0,100)
        val newHappiness = (refreshedCat.stats.happiness + 5).coerceIn(0,100)

        val updatedStats = refreshedCat.stats.copy(
            hygiene = newHygiene,
            happiness = newHappiness,
            lastUpdated = System.currentTimeMillis()
        )

        catRepository.updateCatStats(refreshedCat, updatedStats)

        gameStateRepository.addCatCoins(15)

        return CareActionResult.Success(15)
    }
}