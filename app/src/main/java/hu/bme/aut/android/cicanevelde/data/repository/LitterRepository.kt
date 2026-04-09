package hu.bme.aut.android.cicanevelde.data.repository

import hu.bme.aut.android.cicanevelde.data.dao.LitterStateDao
import hu.bme.aut.android.cicanevelde.data.entity.LitterStateEntity
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.result.item.CleanLitterResult
import hu.bme.aut.android.cicanevelde.domain.result.item.FillLitterResult
import hu.bme.aut.android.cicanevelde.domain.result.item.RemoveItemResult
import kotlinx.coroutines.flow.Flow

class LitterRepository(
    private val litterStateDao: LitterStateDao,
    private val itemRepository: ItemRepository
) {
    fun getAllLitterStates(): Flow<List<LitterStateEntity>> {
        return litterStateDao.getAllLitterStates()
    }

    suspend fun getFirstCleanLitter(): LitterStateEntity? {
        return litterStateDao.getFirstCleanLitter()
    }

    private suspend fun getLitterState(placedItemId: Long): LitterStateEntity? {
        return litterStateDao.getLitterStateByPlacedItemId(placedItemId)
    }

    suspend fun createLitterState(placedItemId: Long): Long {
        val litterState = LitterStateEntity(
            placedItemId = placedItemId,
            isFull = false
        )
        return litterStateDao.insertLitterState(litterState)
    }

    suspend fun deleteLitterState(placedItemId: Long) {
        getLitterState(placedItemId)?.let { litterStateDao.deleteLitterState(it) }
    }

    suspend fun fillLitter(placedItemId: Long): FillLitterResult {
        val litterState = getLitterState(placedItemId) ?: return FillLitterResult.LitterBoxNotFound

        if (litterState.isFull) return FillLitterResult.LitterAlreadyFull

        litterStateDao.updateLitterState(
            litterState.copy(isFull = true)
        )

        return FillLitterResult.Success
    }

    suspend fun cleanLitter(placedItemId: Long): CleanLitterResult {
        val litterState = getLitterState(placedItemId) ?: return CleanLitterResult.LitterBoxNotFound

        if (!litterState.isFull) return CleanLitterResult.LitterAlreadyClean

        return when (itemRepository.removeItem(ItemCode.LITTER)) {
            RemoveItemResult.Success -> {
                litterStateDao.updateLitterState(litterState.copy(isFull = false))
                CleanLitterResult.Success
            }
            RemoveItemResult.ItemNotOwned, RemoveItemResult.InvalidAmount -> CleanLitterResult.NoLitterAvailable
        }
    }
}