package hu.bme.aut.android.cicanevelde.data.model

data class Cat(
    val id: Long,
    val name: String,
    val age: Int,
    val gender: Gender,
    val pattern: Pattern,
    val stats: Stats
)
