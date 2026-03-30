package hu.bme.aut.android.cicanevelde.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hu.bme.aut.android.cicanevelde.data.entity.PlacedItemEntity
import hu.bme.aut.android.cicanevelde.domain.model.enums.RoomType
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacedItemDao {
    @Query("SELECT * FROM placed_items")
    fun getAllPlacedItems(): Flow<List<PlacedItemEntity>>

    @Query("SELECT * FROM placed_items WHERE room = :room")
    fun getPlacedItemsByRoom(room: RoomType): Flow<List<PlacedItemEntity>>

    @Query("SELECT * FROM placed_items WHERE id = :id LIMIT 1")
    suspend fun getPlacedItemById(id: Long): PlacedItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlacedItem(placedItem: PlacedItemEntity): Long

    @Update
    suspend fun updatePlacedItem(placedItem: PlacedItemEntity)

    @Delete
    suspend fun deletePlacedItem(placedItem: PlacedItemEntity)
}