package hu.bme.aut.android.cicanevelde.repository

import hu.bme.aut.android.cicanevelde.data.ItemSeed
import hu.bme.aut.android.cicanevelde.data.dao.ItemDao
import hu.bme.aut.android.cicanevelde.data.dao.OwnedItemDao
import hu.bme.aut.android.cicanevelde.data.entity.ItemEntity
import hu.bme.aut.android.cicanevelde.data.entity.OwnedItemEntity
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

    fun getAllItems(): Flow<List<ItemEntity>> = itemDao.getAllItems()

    fun getOwnedItems(): Flow<List<OwnedItemEntity>> = ownedItemDao.getAllOwnedItems()

    suspend fun getItemById(id: Long): ItemEntity? {
        return itemDao.getItemById(id)
    }

    suspend fun getItemByCode(itemCode: ItemCode): ItemEntity? {
        return itemDao.getItemByCode(itemCode)
    }

    suspend fun getOwnedItemByCode(itemCode: ItemCode): OwnedItemEntity? {
        val item = itemDao.getItemByCode(itemCode) ?: return null
        return ownedItemDao.getOwnedItemByItemId(item.id)
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
                ownedItem.copy(quantity = newQuantity)
            )
        } else {
            ownedItemDao.deleteOwnedItem(ownedItem)
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