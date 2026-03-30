package hu.bme.aut.android.cicanevelde.repository

import hu.bme.aut.android.cicanevelde.data.dao.PlacedItemDao
import hu.bme.aut.android.cicanevelde.data.entity.PlacedItemEntity
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.model.enums.RoomType
import hu.bme.aut.android.cicanevelde.domain.result.PlaceItemResult
import hu.bme.aut.android.cicanevelde.domain.result.RemoveItemResult
import hu.bme.aut.android.cicanevelde.domain.result.RemovePlacedItemResult
import kotlinx.coroutines.flow.Flow

class PlacedItemRepository(
    private val placedItemDao: PlacedItemDao,
    private val itemRepository: ItemRepository,
    private val bowlRepository: BowlRepository
) {
    fun getPlacedItems(): Flow<List<PlacedItemEntity>> = placedItemDao.getAllPlacedItems()

    fun getPlacedItemsInRoom(room: RoomType): Flow<List<PlacedItemEntity>> = placedItemDao.getPlacedItemsByRoom(room)

    suspend fun getPlacedItem(placedItemId: Long): PlacedItemEntity? {
        return placedItemDao.getPlacedItemById(placedItemId)
    }

    suspend fun placeItem(itemCode: ItemCode, room: RoomType, x: Int, y: Int): PlaceItemResult {
        val item = itemRepository.getItemByCode(itemCode) ?: return  PlaceItemResult.ItemNotFound

        if (!item.isPlaceable) return PlaceItemResult.ItemNotPlaceable

        return when (itemRepository.removeItem(itemCode)) {
            RemoveItemResult.Success -> {
                val placedItemId = placedItemDao.insertPlacedItem(
                    PlacedItemEntity(
                        itemId = item.id,
                        room = room,
                        positionX = x,
                        positionY = y
                    )
                )

                if (itemCode == ItemCode.BOWL) bowlRepository.createBowlState(placedItemId)

                PlaceItemResult.Success(placedItemId)
            }
            RemoveItemResult.ItemNotOwned -> PlaceItemResult.ItemNotOwned
            RemoveItemResult.InvalidAmount -> PlaceItemResult.FailedToRemoveFromInventory
        }
    }

    suspend fun removePlacedItem(placedItemId: Long): RemovePlacedItemResult {
        val placedItem = placedItemDao.getPlacedItemById(placedItemId) ?: return RemovePlacedItemResult.PlacedItemNotFound
        val item = itemRepository.getItemById(placedItem.itemId) ?: return RemovePlacedItemResult.PlacedItemNotFound

        if (item.code == ItemCode.BOWL) bowlRepository.deleteBowlState(placedItemId)

        itemRepository.addItem(item.id)

        placedItemDao.deletePlacedItem(placedItem)

        return RemovePlacedItemResult.Success(item.code)
    }
}