package hu.bme.aut.android.cicanevelde.data.repository

import hu.bme.aut.android.cicanevelde.data.ItemSeed
import hu.bme.aut.android.cicanevelde.data.dao.ItemDao
import hu.bme.aut.android.cicanevelde.data.dao.OwnedItemDao
import hu.bme.aut.android.cicanevelde.data.entity.ItemEntity
import hu.bme.aut.android.cicanevelde.data.entity.OwnedItemEntity
import hu.bme.aut.android.cicanevelde.domain.mappers.toDomain
import hu.bme.aut.android.cicanevelde.domain.mappers.toEntity
import hu.bme.aut.android.cicanevelde.domain.model.Item
import hu.bme.aut.android.cicanevelde.domain.model.OwnedItem
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.result.item.BuyItemResult
import hu.bme.aut.android.cicanevelde.domain.result.item.RemoveItemResult
import kotlinx.coroutines.flow.Flow

class ItemRepository(
    private val itemDao: ItemDao,
    private val ownedItemDao: OwnedItemDao,
    private val gameStateRepository: GameStateRepository
) {
    suspend fun initializeDefaultItems() {
        if (itemDao.getItemCount() > 0) return

        itemDao.insertItems(ItemSeed.defaultItems)
    }

    suspend fun getAllItems(): List<Item> {
        val itemEntities = itemDao.getAllItems()
        val items = mutableListOf<Item>()

        for (item in itemEntities) items.add(item.toDomain())

        return items
    }

    suspend fun getOwnedItems(): List<OwnedItem> {
        val ownedItemEntities = ownedItemDao.getAllOwnedItems()
        val ownedItems = mutableListOf<OwnedItem>()

        for (entity in ownedItemEntities) {
            val item = getItemById(entity.itemId)

            item?.let { entity.toDomain(it) }?.let { ownedItems.add(it) }
        }

        return ownedItems
    }

    suspend fun getItemById(id: Long): Item? {
        return itemDao.getItemById(id)?.toDomain()
    }

    suspend fun getItemByCode(itemCode: ItemCode): Item? {
        return itemDao.getItemByCode(itemCode)?.toDomain()
    }

    suspend fun getOwnedItemByCode(itemCode: ItemCode): OwnedItem? {
        val item = itemDao.getItemByCode(itemCode) ?: return null

        return ownedItemDao.getOwnedItemByItemId(item.id)?.toDomain(item.toDomain())
    }

    suspend fun getOwnedQuantityByCode(itemCode: ItemCode): Int {
        val ownedItem = getOwnedItemByCode(itemCode)
        return ownedItem?.quantity ?: 0
    }

    suspend fun addItem(itemId: Long, amount: Int = 1) {
        if (amount <= 0) return

        val ownedItem = ownedItemDao.getOwnedItemByItemId(itemId)

        if (ownedItem == null) {
            ownedItemDao.insertOwnedItem(
                OwnedItemEntity(
                    itemId = itemId,
                    quantity = amount
                )
            )
        } else {
            ownedItemDao.updateOwnedItem(
                ownedItem.copy(quantity = ownedItem.quantity + amount)
            )
        }
    }

    suspend fun removeItem(itemCode: ItemCode, amount: Int = 1): RemoveItemResult {
        if (amount <= 0) return RemoveItemResult.InvalidAmount

        val ownedItem = getOwnedItemByCode(itemCode) ?: return RemoveItemResult.ItemNotOwned

        val newQuantity = ownedItem.quantity - amount

        if (newQuantity > 0) {
            ownedItemDao.updateOwnedItem(
                ownedItem.copy(quantity = newQuantity).toEntity()
            )
        } else {
            ownedItemDao.deleteOwnedItem(ownedItem.toEntity())
        }

        return RemoveItemResult.Success
    }

    suspend fun buyItem(itemCode: ItemCode): BuyItemResult {
        val item = itemDao.getItemByCode(itemCode) ?: return BuyItemResult.ItemNotFound
        val gameState = gameStateRepository.getGameState() ?: return BuyItemResult.GameStateNotFound

        if (gameState.catCoins < item.price) return BuyItemResult.NotEnoughCoins

        gameStateRepository.spendCatCoins(item.price)

        when (itemCode) {
            ItemCode.CAT_SHAMPOO -> addItem(item.id, 5)
            ItemCode.LITTER -> addItem(item.id, 10)
            ItemCode.DRY_FOOD -> addItem(item.id, 10)
            else -> addItem(item.id)
        }

        return BuyItemResult.Success
    }
}