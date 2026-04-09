package hu.bme.aut.android.cicanevelde.domain.usecase

import hu.bme.aut.android.cicanevelde.data.repository.CatRepository
import hu.bme.aut.android.cicanevelde.data.repository.GameStateRepository
import hu.bme.aut.android.cicanevelde.data.repository.ItemRepository
import hu.bme.aut.android.cicanevelde.domain.model.Cat
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.result.CareActionResult
import javax.inject.Inject

class PlayWithCatUseCase @Inject constructor(
    private val catRepository: CatRepository,
    private val itemRepository: ItemRepository,
    private val gameStateRepository: GameStateRepository
) {
    suspend operator fun invoke(cat: Cat): CareActionResult {
        val refreshedCat = catRepository.refreshCatStats(cat)

        if (refreshedCat.stats.energy < 20) return CareActionResult.NotEnoughEnergy

        itemRepository.getOwnedItemByCode(ItemCode.WAND) ?: return CareActionResult.NoItemAvailable

        val newHappiness = (refreshedCat.stats.happiness + 20)
        val newEnergy = (refreshedCat.stats.energy - 15)

        val updatedStats = refreshedCat.stats.copy(
            energy = newEnergy,
            happiness = newHappiness,
            lastUpdated = System.currentTimeMillis()
        )

        catRepository.updateCatStats(refreshedCat, updatedStats)

        gameStateRepository.addCatCoins(20)

        return CareActionResult.Success(20)
    }
}