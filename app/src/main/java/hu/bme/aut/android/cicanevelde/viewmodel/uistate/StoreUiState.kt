package hu.bme.aut.android.cicanevelde.viewmodel.uistate

import hu.bme.aut.android.cicanevelde.ui.components.StoreSectionUi

data class StoreUiState(
    val catCoins: Int = 0,
    val sections: List<StoreSectionUi> = emptyList(),
    val errorMessage: String? = null,
    val successMessage: String? = null
)
