package hu.bme.aut.android.cicanevelde.viewmodel.uistate

import hu.bme.aut.android.cicanevelde.domain.model.enums.Gender
import hu.bme.aut.android.cicanevelde.domain.model.enums.Pattern

data class CreateCatUiState(
    val name: String = "",
    val selectedGender: Gender? = null,
    val selectedPattern: Pattern? = null,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val creationComplete: Boolean = false
)
