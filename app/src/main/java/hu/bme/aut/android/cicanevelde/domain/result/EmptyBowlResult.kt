package hu.bme.aut.android.cicanevelde.domain.result

sealed class EmptyBowlResult {
    data object Success : EmptyBowlResult()
    data object BowlNotFound : EmptyBowlResult()
    data object BowlNotFilled : EmptyBowlResult()
}