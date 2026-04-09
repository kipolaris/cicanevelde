package hu.bme.aut.android.cicanevelde.domain.model

import hu.bme.aut.android.cicanevelde.domain.model.enums.RoomType

data class PlacedItem(
    val id: Long,
    val item: Item,
    val room: RoomType,
    val positionX: Int,
    val positionY: Int
)
