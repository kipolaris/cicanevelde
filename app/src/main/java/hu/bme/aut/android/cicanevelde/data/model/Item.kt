package hu.bme.aut.android.cicanevelde.data.model

data class Item(
    val id: Long,
    val name: String,
    val price: Int,
    val type: ItemType
)
