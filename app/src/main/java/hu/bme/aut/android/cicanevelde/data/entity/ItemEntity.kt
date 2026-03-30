package hu.bme.aut.android.cicanevelde.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemType

@Entity(
    tableName = "items",
    indices = [Index(value = ["code"], unique = true)]
)
data class ItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val code: ItemCode,
    val price: Int,
    val type: ItemType,
    val isPlaceable: Boolean
)
