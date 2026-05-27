package hu.bme.aut.android.cicanevelde.viewmodel.uistate

import hu.bme.aut.android.cicanevelde.domain.model.Cat
import hu.bme.aut.android.cicanevelde.domain.model.enums.CatActivity
import hu.bme.aut.android.cicanevelde.domain.model.enums.RoomType
import hu.bme.aut.android.cicanevelde.ui.components.CatActionUi
import hu.bme.aut.android.cicanevelde.ui.components.PlacedItemUi

data class HomeUiState(
    val isLoading: Boolean = true,
    val cat: Cat? = null,
    val currentRoom: RoomType = RoomType.LIVINGROOM,
    val placedItems: List<PlacedItemUi> = emptyList(),
    val catActions: List<CatActionUi> = emptyList(),
    val isCatActionMenuVisible: Boolean = false,
    val isCatStatsPopupVisible: Boolean = false,
    val catRoom: RoomType = RoomType.LIVINGROOM,
    val catActivity: CatActivity = CatActivity.IDLE,
    val catX: Int = 860,
    val catY: Int = 650,
    val errorMessage: String? = null
)