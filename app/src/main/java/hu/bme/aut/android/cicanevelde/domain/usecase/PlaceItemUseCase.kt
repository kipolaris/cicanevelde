package hu.bme.aut.android.cicanevelde.domain.usecase

import hu.bme.aut.android.cicanevelde.data.repository.BowlRepository
import hu.bme.aut.android.cicanevelde.data.repository.LitterRepository
import hu.bme.aut.android.cicanevelde.data.repository.PlacedItemRepository
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.model.enums.RoomType
import hu.bme.aut.android.cicanevelde.domain.result.item.PlaceItemResult
import javax.inject.Inject

class PlaceItemUseCase @Inject constructor(
    private val placedItemRepository: PlacedItemRepository,
    private val bowlRepository: BowlRepository,
    private val litterRepository: LitterRepository
) {
    suspend operator fun invoke(
        itemCode: ItemCode,
        room: RoomType,
        x: Int,
        y: Int
    ): PlaceItemResult {
        val result = placedItemRepository.placeItem(itemCode, room, x, y)

        if (result is PlaceItemResult.Success) {
            when (itemCode) {
                ItemCode.BOWL -> bowlRepository.createBowlState(result.placedItemId)
                ItemCode.LITTER_BOX -> litterRepository.createLitterState(result.placedItemId)
                else -> Unit
            }
        }

        return result
    }
}