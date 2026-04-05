package hu.bme.aut.android.cicanevelde.repository

import hu.bme.aut.android.cicanevelde.data.dao.BowlStateDao
import hu.bme.aut.android.cicanevelde.data.entity.BowlStateEntity
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemType
import hu.bme.aut.android.cicanevelde.domain.result.item.EmptyBowlResult
import hu.bme.aut.android.cicanevelde.domain.result.item.FillBowlResult
import hu.bme.aut.android.cicanevelde.domain.result.item.RemoveItemResult
import kotlinx.coroutines.flow.Flow

class BowlRepository(
    private val bowlStateDao: BowlStateDao,
    private val itemRepository: ItemRepository
) {
    fun getAllBowlStates(): Flow<List<BowlStateEntity>> {
        return bowlStateDao.getAllBowlStates()
    }

    private suspend fun getBowlState(placedItemId: Long): BowlStateEntity? {
        return bowlStateDao.getBowlStateByPlacedItemId(placedItemId)
    }

    suspend fun getFirstFilledBowl(): BowlStateEntity? {
        return bowlStateDao.getFirstFilledBowl()
    }

    suspend fun createBowlState(placedItemId: Long): Long {
        val bowlState = BowlStateEntity(
            placedItemId = placedItemId,
            isFilled = false,
            foodItemId = null
        )
        return bowlStateDao.insertBowlState(bowlState)
    }

    suspend fun deleteBowlState(placedItemId: Long) {
        getBowlState(placedItemId)?.let { bowlStateDao.deleteBowlState(it) }
    }

    suspend fun fillBowl(placedItemId: Long, foodCode: ItemCode): FillBowlResult {
        val bowlState = getBowlState(placedItemId) ?: return FillBowlResult.BowlNotFound

        if (bowlState.isFilled) return FillBowlResult.BowlAlreadyFull

        val food = itemRepository.getItemByCode(foodCode) ?: return FillBowlResult.NoFoodAvailable

        if (food.type != ItemType.FOOD) return FillBowlResult.NoFoodAvailable

        if (itemRepository.getOwnedQuantityByCode(foodCode) <= 0) return FillBowlResult.NoFoodAvailable

        return when (itemRepository.removeItem(foodCode)) {
            RemoveItemResult.Success -> {
                bowlStateDao.updateBowlState(bowlState.copy(isFilled = true, foodItemId = food.id))
                FillBowlResult.Success
            }
            RemoveItemResult.ItemNotOwned, RemoveItemResult.InvalidAmount -> {
                FillBowlResult.NoFoodAvailable
            }
        }
    }

    suspend fun emptyBowl(placedItemId: Long): EmptyBowlResult {
        val bowlState = getBowlState(placedItemId) ?: return EmptyBowlResult.BowlNotFound

        if (!bowlState.isFilled) return EmptyBowlResult.BowlNotFilled

        bowlStateDao.updateBowlState(
            bowlState.copy(isFilled = false, foodItemId = null)
        )

        return EmptyBowlResult.Success
    }
}