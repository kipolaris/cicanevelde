package hu.bme.aut.android.cicanevelde.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.bme.aut.android.cicanevelde.data.model.Gender
import hu.bme.aut.android.cicanevelde.data.model.Pattern
import hu.bme.aut.android.cicanevelde.data.model.Stats

@Entity(tableName = "cats")
data class CatEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val age: Int,
    val gender: Gender,
    val pattern: Pattern,

    @Embedded(prefix = "_stats")
    val stats: Stats
)
