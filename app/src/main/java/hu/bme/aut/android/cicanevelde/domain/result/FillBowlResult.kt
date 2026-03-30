package hu.bme.aut.android.cicanevelde.domain.result

sealed class FillBowlResult {
    data object Success : FillBowlResult()
    data object BowlAlreadyFull : FillBowlResult()
    data object BowlNotFound : FillBowlResult()
    data object NoFoodAvailable : FillBowlResult()
}