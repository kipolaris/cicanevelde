package hu.bme.aut.android.cicanevelde.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.cicanevelde.data.repository.CatRepository
import hu.bme.aut.android.cicanevelde.data.repository.GameStateRepository
import hu.bme.aut.android.cicanevelde.data.repository.PlacedItemRepository
import hu.bme.aut.android.cicanevelde.domain.model.PlacedItem
import hu.bme.aut.android.cicanevelde.domain.model.enums.InteractionType
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.model.enums.RoomType
import hu.bme.aut.android.cicanevelde.viewmodel.uistate.HomeUiState
import hu.bme.aut.android.cicanevelde.ui.components.PlacedItemUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gameStateRepository: GameStateRepository,
    private val catRepository: CatRepository,
    private val placedItemRepository: PlacedItemRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val rooms = listOf(
        RoomType.LIVINGROOM,
        RoomType.KITCHEN,
        RoomType.BATHROOM
    )

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val gameState = gameStateRepository.getGameState()
                val selectedCatId = gameState?.selectedCatId

                if (selectedCatId == null) {
                    _uiState.value = HomeUiState(
                        isLoading = false,
                        cat = null,
                        errorMessage = "No selected cat found"
                    )
                    return@launch
                }

                val cat = catRepository.getCatById(selectedCatId)

                if (cat == null) {
                    _uiState.value = HomeUiState(
                        isLoading = false,
                        cat = null,
                        errorMessage = "Cat not found"
                    )
                    return@launch
                }

                val refreshedCat = catRepository.refreshCatStats(cat)

                val placedItems = loadPlacedItemUi(gameState.currentRoom)

                _uiState.value = HomeUiState(
                    isLoading = false,
                    cat = refreshedCat,
                    currentRoom = gameState.currentRoom,
                    placedItems = placedItems,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState(
                    isLoading = false,
                    errorMessage = e.message ?: "Failed to load home"
                )
            }
        }
    }

    fun selectRoom(room: RoomType) {
        viewModelScope.launch {
            gameStateRepository.setCurrentRoom(room)

            val placedItems = loadPlacedItemUi(room)

            _uiState.value = _uiState.value.copy(currentRoom = room, placedItems = placedItems)
        }
    }

    private fun changeRoomByOffset(offset: Int) {
        val currentRoom = _uiState.value.currentRoom
        val currentIndex = rooms.indexOf(currentRoom)

        val nextIndex = if (currentIndex == -1) {
            0
        } else {
            (currentIndex + offset + rooms.size) % rooms.size
        }

        selectRoom(rooms[nextIndex])
    }

    fun goToNextRoom() = changeRoomByOffset(1)

    fun goToPreviousRoom() = changeRoomByOffset(-1)

    fun refreshCat() {
        val cat = _uiState.value.cat ?: return

        viewModelScope.launch {
            try {
                val refreshedCat = catRepository.refreshCatStats(cat)

                _uiState.value = _uiState.value.copy(
                    cat = refreshedCat,
                    errorMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to refresh cat"
                )
            }
        }
    }



    private suspend fun loadPlacedItemUi(room: RoomType): List<PlacedItemUi> {
        val placedItems = placedItemRepository.getPlacedItemsInRoom(room)

        return placedItems.map { placedItem ->
            PlacedItemUi(
                placedItem = placedItem,
                interactionType = getInteractionType(placedItem)
            )
        }
    }

    private fun getInteractionType(placedItem: PlacedItem): InteractionType? {
        return when (placedItem.item.code) {
            ItemCode.BOWL -> InteractionType.FILL_BOWL
            ItemCode.WAND -> InteractionType.PLAY
            ItemCode.LITTER_BOX -> InteractionType.CLEAN_LITTER
            else -> null
        }
    }

    fun onPlacedItemClicked(itemUi: PlacedItemUi) {
        when (itemUi.interactionType) {
            InteractionType.FILL_BOWL -> {
                // later: fill bowl logic
            }

            InteractionType.CLEAN_LITTER -> {
                // later: clean litter logic
            }

            InteractionType.PLAY -> {
                // later: play logic
            }

            null -> Unit
        }
    }

    fun onCatClicked() {
        _uiState.value = _uiState.value.copy(
            isCatStatsPopupVisible = !_uiState.value.isCatStatsPopupVisible
        )
    }

    fun onStatsPopupClicked() {
        _uiState.value = _uiState.value.copy(
            isCatStatsPopupVisible = false
        )
    }
}