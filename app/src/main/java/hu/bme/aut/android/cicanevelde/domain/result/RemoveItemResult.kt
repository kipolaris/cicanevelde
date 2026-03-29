package hu.bme.aut.android.cicanevelde.domain.result

sealed class RemoveItemResult {
    data object Success : RemoveItemResult()
    data object ItemNotOwned : RemoveItemResult()
    data object InvalidAmount : RemoveItemResult()
}