package hu.bme.aut.android.cicanevelde.domain.model

import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemType

data class Item(
    val id: Long,
    val name: String,
    val code: ItemCode,
    val price: Int,
    val type: ItemType
)
