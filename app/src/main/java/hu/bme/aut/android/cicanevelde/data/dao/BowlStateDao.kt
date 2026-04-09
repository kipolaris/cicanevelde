package hu.bme.aut.android.cicanevelde.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hu.bme.aut.android.cicanevelde.data.entity.BowlStateEntity

@Dao
interface BowlStateDao {
    @Query("SELECT * FROM bowl_states")
    suspend fun getAllBowlStates(): List<BowlStateEntity>

    @Query("SELECT * FROM bowl_states WHERE placedItemId = :placedItemId LIMIT 1")
    suspend fun getBowlStateByPlacedItemId(placedItemId: Long): BowlStateEntity?

    @Query("SELECT * FROM bowl_states WHERE foodItemId IS NOT NULL ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomFilledBowl(): BowlStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBowlState(bowlState: BowlStateEntity): Long

    @Update
    suspend fun updateBowlState(bowlState: BowlStateEntity)

    @Delete
    suspend fun deleteBowlState(bowlState: BowlStateEntity)
}