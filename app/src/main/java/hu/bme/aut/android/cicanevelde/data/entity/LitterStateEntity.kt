package hu.bme.aut.android.cicanevelde.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "litter_states",
    foreignKeys = [
        ForeignKey(
            entity = PlacedItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["placedItemId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["placedItemId"], unique = true)]
)
data class LitterStateEntity(
    @PrimaryKey
    val id: Long = 0,
    val placedItemId: Long,
    val isFull: Boolean = false
)