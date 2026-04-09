package hu.bme.aut.android.cicanevelde.domain.model

data class BowlState(
    val id: Long,
    val placedItem: PlacedItem,
    val food: Item?
)
