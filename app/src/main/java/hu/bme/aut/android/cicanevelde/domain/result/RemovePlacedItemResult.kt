package hu.bme.aut.android.cicanevelde.domain.result

import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode

sealed class RemovePlacedItemResult {
    data class Success(val itemCode: ItemCode) : RemovePlacedItemResult()
    data object PlacedItemNotFound : RemovePlacedItemResult()
}