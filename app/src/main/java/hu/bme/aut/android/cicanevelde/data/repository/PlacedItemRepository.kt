package hu.bme.aut.android.cicanevelde.data.repository

import hu.bme.aut.android.cicanevelde.data.dao.PlacedItemDao
import hu.bme.aut.android.cicanevelde.data.entity.PlacedItemEntity
import hu.bme.aut.android.cicanevelde.domain.mappers.toDomain
import hu.bme.aut.android.cicanevelde.domain.model.PlacedItem
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.model.enums.RoomType
import hu.bme.aut.android.cicanevelde.domain.result.item.PlaceItemResult
import hu.bme.aut.android.cicanevelde.domain.result.item.RemoveItemResult
import hu.bme.aut.android.cicanevelde.domain.result.item.RemovePlacedItemResult
import kotlinx.coroutines.flow.Flow

class PlacedItemRepository(
    private val placedItemDao: PlacedItemDao,
    private val itemRepository: ItemRepository,
    private val bowlRepository: BowlRepository,
    private val litterRepository: LitterRepository
) {
    suspend fun getPlacedItems(): List<PlacedItem> {
        val placedItemEntities = placedItemDao.getAllPlacedItems()
        val placedItems = mutableListOf<PlacedItem>()

        for (entity in placedItemEntities) {
            val item = itemRepository.getItemById(entity.itemId)

            item?.let { entity.toDomain(it) }?.let { placedItems.add(it) }
        }

        return placedItems
    }

    //suspend fun getPlacedItemsInRoom(room: RoomType): List<PlacedItemEntity> = placedItemDao.getPlacedItemsByRoom(room)

    suspend fun getPlacedItemsInRoom(room: RoomType): List<PlacedItem> {
        val placedItemEntities = placedItemDao.getPlacedItemsByRoom(room)
        val placedItems = mutableListOf<PlacedItem>()

        for (entity in placedItemEntities) {
            val item = itemRepository.getItemById(entity.itemId)

            item?.let { entity.toDomain(it) }?.let { placedItems.add(it) }
        }

        return placedItems
    }

    suspend fun getPlacedItem(placedItemId: Long): PlacedItem? {
        val placedItemEntity = placedItemDao.getPlacedItemById(placedItemId)
        val item = placedItemEntity?.let { itemRepository.getItemById(it.itemId) }

        return item?.let { placedItemEntity.toDomain(it) }
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
                if (itemCode == ItemCode.LITTER_BOX) litterRepository.createLitterState(placedItemId)

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
        if (item.code == ItemCode.LITTER_BOX) litterRepository.deleteLitterState(placedItemId)

        itemRepository.addItem(item.id)

        placedItemDao.deletePlacedItem(placedItem)

        return RemovePlacedItemResult.Success(item.code)
    }
}