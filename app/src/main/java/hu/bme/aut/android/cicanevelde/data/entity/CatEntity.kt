package hu.bme.aut.android.cicanevelde.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import hu.bme.aut.android.cicanevelde.domain.model.enums.Gender
import hu.bme.aut.android.cicanevelde.domain.model.enums.Pattern
import hu.bme.aut.android.cicanevelde.domain.model.Stats

@Entity(tableName = "cats")
data class CatEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val age: Int = 0,
    val gender: Gender,
    val pattern: Pattern,

    @Embedded(prefix = "_stats")
    val stats: Stats
)
