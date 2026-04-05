package hu.bme.aut.android.cicanevelde.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hu.bme.aut.android.cicanevelde.data.entity.LitterStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LitterStateDao {
    @Query("SELECT * FROM litter_states")
    fun getAllLitterStates(): Flow<List<LitterStateEntity>>

    @Query("SELECT * FROM litter_states WHERE isFull = 0 LIMIT 1")
    fun getFirstCleanLitter(): LitterStateEntity?

    @Query("SELECT * FROM litter_states WHERE placedItemId = :placedItemId LIMIT 1")
    suspend fun getLitterStateByPlacedItemId(placedItemId: Long): LitterStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLitterState(litterState: LitterStateEntity): Long

    @Update
    suspend fun updateLitterState(litterState: LitterStateEntity)

    @Delete
    suspend fun deleteLitterState(litterState: LitterStateEntity)
}