package hu.bme.aut.android.cicanevelde.repository

import hu.bme.aut.android.cicanevelde.data.dao.GameStateDao
import hu.bme.aut.android.cicanevelde.data.dao.ItemDao
import hu.bme.aut.android.cicanevelde.data.dao.OwnedItemDao
import hu.bme.aut.android.cicanevelde.data.entity.ItemEntity
import hu.bme.aut.android.cicanevelde.data.entity.OwnedItemEntity
import hu.bme.aut.android.cicanevelde.domain.result.BuyItemResult
import hu.bme.aut.android.cicanevelde.domain.result.RemoveItemResult
import kotlinx.coroutines.flow.Flow

class ItemRepository(
    private val itemDao: ItemDao,
    private val ownedItemDao: OwnedItemDao,
    private val gameStateDao: GameStateDao
) {
    fun getAllItems(): Flow<List<ItemEntity>> = itemDao.getAllItems()

    fun getOwnedItems(): Flow<List<OwnedItemEntity>> = ownedItemDao.getAllOwnedItems()

    /*suspend fun getOwnedQuantity(itemId: Long): Int {
        val ownedItem = ownedItemDao.getOwnedItemByItemId(itemId)
        return ownedItem?.quantity ?: 0
    }

    suspend fun hasItem(itemId: Long): Boolean {
        return getOwnedQuantity(itemId) > 0
    }*/

    private suspend fun addItem(itemId: Long, amount: Int = 1) {
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

    suspend fun removeItem(itemId: Long, amount: Int = 1): RemoveItemResult {
        if (amount <= 0) return RemoveItemResult.InvalidAmount

        val ownedItem = ownedItemDao.getOwnedItemByItemId(itemId) ?: return RemoveItemResult.ItemNotOwned

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

    suspend fun buyItem(itemId: Long): BuyItemResult {
        val item = itemDao.getItemById(itemId) ?: return BuyItemResult.ItemNotFound
        val gameState = gameStateDao.getGameState() ?: return BuyItemResult.GameStateNotFound

        if (gameState.catCoins < item.price) return BuyItemResult.NotEnoughCoins

        gameStateDao.updateGameState(
            gameState.copy(catCoins = gameState.catCoins - item.price)
        )

        addItem(itemId)

        return BuyItemResult.Success
    }
}