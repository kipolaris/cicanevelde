package hu.bme.aut.android.cicanevelde.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.bme.aut.android.cicanevelde.domain.model.enums.RoomType

@Entity(tableName = "game_state")
data class GameStateEntity(
    @PrimaryKey val id: Int = 0,
    val catCoins: Int = 0,
    val isInitialized: Boolean,
    val selectedCatId: Long?,
    val currentRoom: RoomType
)
