package hu.bme.aut.android.cicanevelde.ui.components

import hu.bme.aut.android.cicanevelde.domain.model.enums.CatActionType

data class CatActionUi(
    val label: String,
    val type: CatActionType
)
