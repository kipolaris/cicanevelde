package hu.bme.aut.android.cicanevelde.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hu.bme.aut.android.cicanevelde.data.entity.CatEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CatDao {
    @Query("SELECT * FROM cats")
    fun getAllCats(): Flow<List<CatEntity>>

    @Query("SELECT COUNT(*) FROM cats")
    suspend fun getCatCount(): Int

    @Query("SELECT * FROM cats WHERE id = :id")
    suspend fun getCatById(id: Long): CatEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCat(cat: CatEntity): Long

    @Update
    suspend fun updateCat(cat: CatEntity)

    @Delete
    suspend fun deleteCat(cat: CatEntity)
}