package hu.bme.aut.android.cicanevelde.domain.model

import hu.bme.aut.android.cicanevelde.domain.model.enums.RoomType

data class GameState(
    val id: Int = 0,
    val catCoins: Int,
    val isInitialized: Boolean,
    val selectedCatId: Long?,
    val currentRoom: RoomType
)
