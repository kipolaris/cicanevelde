package hu.bme.aut.android.cicanevelde.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bowl_states",
    foreignKeys = [
        ForeignKey(
            entity = PlacedItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["placedItemId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["foodItemId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["placedItemId"], unique = true), Index("foodItemId")]
)
data class BowlStateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val placedItemId: Long,
    val isFilled: Boolean = false,
    val foodItemId: Long? = null
)
