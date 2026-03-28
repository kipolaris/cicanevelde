package hu.bme.aut.android.cicanevelde.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "owned_items")
data class OwnedItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val quantity: Int
)
