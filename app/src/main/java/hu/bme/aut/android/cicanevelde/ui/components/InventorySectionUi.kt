package hu.bme.aut.android.cicanevelde.ui.components

import hu.bme.aut.android.cicanevelde.domain.model.OwnedItem

data class InventorySectionUi(
    val title: String,
    val items: List<OwnedItem>
)
