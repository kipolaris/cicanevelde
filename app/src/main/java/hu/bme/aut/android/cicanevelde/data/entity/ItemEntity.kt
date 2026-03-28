package hu.bme.aut.android.cicanevelde.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.bme.aut.android.cicanevelde.data.model.ItemType

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,

    val name: String,
    val price: Int,
    val type: ItemType
)
