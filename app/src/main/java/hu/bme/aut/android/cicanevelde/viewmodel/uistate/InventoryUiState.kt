package hu.bme.aut.android.cicanevelde.viewmodel.uistate

import hu.bme.aut.android.cicanevelde.ui.components.InventorySectionUi

data class InventoryUiState(
    val sections: List<InventorySectionUi> = emptyList(),
    val errorMessage: String? = null,
    val successMessage: String? = null
)
