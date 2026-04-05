package hu.bme.aut.android.cicanevelde.domain.result

sealed class EatFoodResult {
    data object Success : EatFoodResult()
    data object NotHungryEnough : EatFoodResult()
    data object NoFoodAvailable : EatFoodResult()
}