package hu.bme.aut.android.cicanevelde.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import hu.bme.aut.android.cicanevelde.domain.model.enums.RoomType

@Entity(
    tableName = "placed_items",
    foreignKeys = [
        ForeignKey(
            entity = ItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["itemId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("itemId")]
)
data class PlacedItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val itemId: Long,
    val room: RoomType,
    val positionX: Int,
    val positionY: Int
)