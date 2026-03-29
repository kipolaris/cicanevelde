package hu.bme.aut.android.cicanevelde.domain.result

sealed class BuyItemResult {
    data object Success : BuyItemResult()
    data object ItemNotFound : BuyItemResult()
    data object GameStateNotFound : BuyItemResult()
    data object NotEnoughCoins : BuyItemResult()
}