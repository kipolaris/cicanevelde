package hu.bme.aut.android.cicanevelde.viewmodel.uistate

import hu.bme.aut.android.cicanevelde.domain.model.PlacedItem
import hu.bme.aut.android.cicanevelde.domain.model.enums.InteractionType

data class PlacedItemUi(
    val placedItem: PlacedItem,
    val isHighlighted: Boolean = false,
    val interactionType: InteractionType? = null
)
