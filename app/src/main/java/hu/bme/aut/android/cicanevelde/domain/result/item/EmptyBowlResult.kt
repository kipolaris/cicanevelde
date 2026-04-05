package hu.bme.aut.android.cicanevelde.domain.result.item

sealed class EmptyBowlResult {
    data object Success : EmptyBowlResult()
    data object BowlNotFound : EmptyBowlResult()
    data object BowlNotFilled : EmptyBowlResult()
}