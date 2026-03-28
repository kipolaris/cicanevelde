package hu.bme.aut.android.cicanevelde.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hu.bme.aut.android.cicanevelde.data.entity.OwnedItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OwnedItemDao {
    @Query("SELECT * FROM owned_items")
    fun getAllOwnedItems(): Flow<List<OwnedItemEntity>>

    @Query("SELECT * FROM owned_items WHERE id = :id")
    suspend fun getOwnedItemById(id: Long): OwnedItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOwnedItem(ownedItem: OwnedItemEntity): Long

    @Update
    suspend fun updateOwnedItem(ownedItem: OwnedItemEntity)

    @Delete
    suspend fun deleteOwnedItem(ownedItem: OwnedItemEntity)
}