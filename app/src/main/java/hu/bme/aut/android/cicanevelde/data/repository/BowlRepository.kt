package hu.bme.aut.android.cicanevelde.data.repository

import hu.bme.aut.android.cicanevelde.data.dao.BowlStateDao
import hu.bme.aut.android.cicanevelde.data.entity.BowlStateEntity
import hu.bme.aut.android.cicanevelde.domain.mappers.toDomain
import hu.bme.aut.android.cicanevelde.domain.mappers.toEntity
import hu.bme.aut.android.cicanevelde.domain.model.BowlState
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemType
import hu.bme.aut.android.cicanevelde.domain.result.item.EmptyBowlResult
import hu.bme.aut.android.cicanevelde.domain.result.item.FillBowlResult
import hu.bme.aut.android.cicanevelde.domain.result.item.RemoveItemResult

class BowlRepository(
    private val bowlStateDao: BowlStateDao,
    private val itemRepository: ItemRepository,
    private val placedItemRepository: PlacedItemRepository
) {
    suspend fun getAllBowlStates(): List<BowlState> {
        val bowlStateEntities = bowlStateDao.getAllBowlStates()
        val bowlStates = mutableListOf<BowlState>()

        for (entity in bowlStateEntities) {
            val placedItem = placedItemRepository.getPlacedItem(entity.placedItemId)
            val food = entity.foodItemId?.let { itemRepository.getItemById(it) }

            placedItem?.let { entity.toDomain(it,food) }?.let { bowlStates.add(it) }
        }

        return bowlStates
    }

    private suspend fun getBowlState(placedItemId: Long): BowlState? {
        val bowlStateEntity = bowlStateDao.getBowlStateByPlacedItemId(placedItemId)

        val placedItem = placedItemRepository.getPlacedItem(placedItemId)
        val food = bowlStateEntity?.foodItemId?.let { itemRepository.getItemById(it) }

        return if (bowlStateEntity != null) {
            placedItem?.let { bowlStateEntity.toDomain(it,food) }
        } else null
    }

    suspend fun getRandomFilledBowl(): BowlState? {
        val bowlEntity = bowlStateDao.getRandomFilledBowl()

        val placedItem = bowlEntity?.let { placedItemRepository.getPlacedItem(it.placedItemId) }
        val food = bowlEntity?.foodItemId?.let { itemRepository.getItemById(it) }

        return placedItem?.let { bowlEntity.toDomain(it,food) }

    }

    suspend fun createBowlState(placedItemId: Long): Long {
        val bowlState = BowlStateEntity(
            placedItemId = placedItemId,
            foodItemId = null
        )
        return bowlStateDao.insertBowlState(bowlState)
    }

    suspend fun deleteBowlState(placedItemId: Long) {
        val bowlState = bowlStateDao.getBowlStateByPlacedItemId(placedItemId)

        if (bowlState != null) {
            bowlStateDao.deleteBowlState(bowlState)
        }
    }

    suspend fun fillBowl(placedItemId: Long, foodCode: ItemCode): FillBowlResult {
        val bowlState = getBowlState(placedItemId) ?: return FillBowlResult.BowlNotFound

        if (bowlState.food != null) return FillBowlResult.BowlAlreadyFull

        val food = itemRepository.getItemByCode(foodCode) ?: return FillBowlResult.NoFoodAvailable

        if (food.type != ItemType.FOOD) return FillBowlResult.NoFoodAvailable

        if (itemRepository.getOwnedQuantityByCode(foodCode) <= 0) return FillBowlResult.NoFoodAvailable

        return when (itemRepository.removeItem(foodCode)) {
            RemoveItemResult.Success -> {
                bowlStateDao.updateBowlState(bowlState.copy(food = food).toEntity())
                FillBowlResult.Success
            }
            RemoveItemResult.ItemNotOwned, RemoveItemResult.InvalidAmount -> {
                FillBowlResult.NoFoodAvailable
            }
        }
    }

    suspend fun emptyBowl(placedItemId: Long): EmptyBowlResult {
        val bowlState = getBowlState(placedItemId) ?: return EmptyBowlResult.BowlNotFound

        if (bowlState.food == null) return EmptyBowlResult.BowlNotFilled

        bowlStateDao.updateBowlState(
            bowlState.copy(food = null).toEntity()
        )

        return EmptyBowlResult.Success
    }
}