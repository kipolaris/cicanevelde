package hu.bme.aut.android.cicanevelde.domain.model

import hu.bme.aut.android.cicanevelde.domain.model.enums.Gender
import hu.bme.aut.android.cicanevelde.domain.model.enums.Pattern

data class Cat(
    val id: Long,
    val name: String,
    val age: Int,
    val gender: Gender,
    val pattern: Pattern,
    val stats: Stats
)
