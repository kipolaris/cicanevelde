package hu.bme.aut.android.cicanevelde.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hu.bme.aut.android.cicanevelde.data.entity.GameStateEntity

@Dao
interface GameStateDao {
    @Query("SELECT * FROM game_state WHERE id = 0")
    suspend fun getGameState(): GameStateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGameState(gameState: GameStateEntity)

    @Update
    suspend fun updateGameState(gameState: GameStateEntity)
}