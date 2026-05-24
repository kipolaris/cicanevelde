package hu.bme.aut.android.cicanevelde.domain.usecase

import hu.bme.aut.android.cicanevelde.data.repository.BowlRepository
import hu.bme.aut.android.cicanevelde.data.repository.LitterRepository
import hu.bme.aut.android.cicanevelde.data.repository.PlacedItemRepository
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.result.item.RemovePlacedItemResult
import javax.inject.Inject

class RemovePlacedItemUseCase @Inject constructor(
    private val placedItemRepository: PlacedItemRepository,
    private val bowlRepository: BowlRepository,
    private val litterRepository: LitterRepository
) {
    suspend operator fun invoke(placedItemId: Long): RemovePlacedItemResult {
        val placedItem = placedItemRepository.getPlacedItem(placedItemId)
            ?: return RemovePlacedItemResult.PlacedItemNotFound

        when (placedItem.item.code) {
            ItemCode.BOWL -> bowlRepository.deleteBowlState(placedItemId)
            ItemCode.LITTER_BOX -> litterRepository.deleteLitterState(placedItemId)
            else -> Unit
        }

        return placedItemRepository.removePlacedItem(placedItemId)
    }
}