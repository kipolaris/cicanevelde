package hu.bme.aut.android.cicanevelde.domain.model

data class LitterState(
    val id: Long,
    val placedItem: PlacedItem,
    val isFull: Boolean
)
