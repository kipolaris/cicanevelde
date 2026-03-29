package hu.bme.aut.android.cicanevelde.domain.model

data class Stats(
    val energy: Int,
    val hunger: Int,
    val bladder: Int,
    val hygiene: Int,
    val happiness: Int,
    val lastUpdated: Long = System.currentTimeMillis()
)
