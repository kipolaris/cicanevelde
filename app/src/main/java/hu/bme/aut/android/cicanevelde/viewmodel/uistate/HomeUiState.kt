package hu.bme.aut.android.cicanevelde.viewmodel.uistate

import hu.bme.aut.android.cicanevelde.domain.model.Cat
import hu.bme.aut.android.cicanevelde.domain.model.enums.RoomType

data class HomeUiState(
    val isLoading: Boolean = true,
    val cat: Cat? = null,
    val currentRoom: RoomType = RoomType.LIVINGROOM,
    val placedItems: List<PlacedItemUi> = emptyList(),
    val isCatActionMenuVisible: Boolean = false,
    val errorMessage: String? = null
)