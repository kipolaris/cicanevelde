package hu.bme.aut.android.cicanevelde.ui.components

import hu.bme.aut.android.cicanevelde.domain.model.PlacedItem
import hu.bme.aut.android.cicanevelde.domain.model.enums.InteractionType

data class PlacedItemUi(
    val placedItem: PlacedItem,
    val interactionType: InteractionType? = null
)
