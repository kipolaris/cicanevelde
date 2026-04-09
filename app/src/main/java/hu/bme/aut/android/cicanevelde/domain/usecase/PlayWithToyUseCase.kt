package hu.bme.aut.android.cicanevelde.domain.usecase

import hu.bme.aut.android.cicanevelde.data.entity.CatEntity
import hu.bme.aut.android.cicanevelde.data.repository.CatRepository
import hu.bme.aut.android.cicanevelde.data.repository.ItemRepository
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.result.CareActionResult
import javax.inject.Inject

class PlayWithToyUseCase @Inject constructor(
    private val catRepository: CatRepository,
    private val itemRepository: ItemRepository
){
    suspend operator fun invoke(cat: CatEntity): CareActionResult {
        val refreshedCat = catRepository.refreshCatStats(cat)

        if (refreshedCat.stats.energy < 20) return CareActionResult.NotEnoughEnergy

        val mouse = itemRepository.getOwnedItemByCode(ItemCode.MOUSE)
        val ball = itemRepository.getOwnedItemByCode(ItemCode.BALL)

        if (mouse == null && ball == null) return CareActionResult.NoItemAvailable

        val newHappiness = (refreshedCat.stats.happiness + 10).coerceIn(0,100)
        val newEnergy = (refreshedCat.stats.energy - 10).coerceIn(0,100)

        val updatedStats = refreshedCat.stats.copy(
            happiness = newHappiness,
            energy = newEnergy,
            lastUpdated = System.currentTimeMillis()
        )

        catRepository.updateCatStats(refreshedCat, updatedStats)

        return CareActionResult.Success()
    }
}