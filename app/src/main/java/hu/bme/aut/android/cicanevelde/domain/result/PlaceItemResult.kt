package hu.bme.aut.android.cicanevelde.domain.result

sealed class PlaceItemResult {
    data class Success(val placedItemId: Long) : PlaceItemResult()
    data object InvalidPosition : PlaceItemResult()
    data object ItemNotFound : PlaceItemResult()
    data object ItemNotOwned : PlaceItemResult()
    data object ItemNotPlaceable : PlaceItemResult()
    data object FailedToRemoveFromInventory : PlaceItemResult()
}