package hu.bme.aut.android.cicanevelde.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.cicanevelde.data.repository.ItemRepository
import hu.bme.aut.android.cicanevelde.data.repository.PlacedItemRepository
import hu.bme.aut.android.cicanevelde.domain.model.OwnedItem
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemType
import hu.bme.aut.android.cicanevelde.domain.model.enums.RoomType
import hu.bme.aut.android.cicanevelde.domain.result.item.PlaceItemResult
import hu.bme.aut.android.cicanevelde.domain.usecase.PlaceItemUseCase
import hu.bme.aut.android.cicanevelde.ui.components.InventorySectionUi
import hu.bme.aut.android.cicanevelde.ui.components.StoreSectionUi
import hu.bme.aut.android.cicanevelde.viewmodel.uistate.InventoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InventoryViewModel @Inject constructor(
    private val itemRepository: ItemRepository,
    private val placeItemUseCase: PlaceItemUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(InventoryUiState())
    val uiState: StateFlow<InventoryUiState> = _uiState

    init {
        loadInventory()
    }

    fun loadInventory() {
        viewModelScope.launch {
            try {
                val ownedItems = itemRepository.getOwnedItems()

                _uiState.value = InventoryUiState(
                    sections = ownedItems.toItemSections(),
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to load inventory"
                )
            }
        }
    }

    fun placeItem(ownedItem: OwnedItem) {
        if (!ownedItem.item.isPlaceable) return

        viewModelScope.launch {
            try {
                val result = placeItemUseCase(
                    itemCode = ownedItem.item.code,
                    room = getDefaultRoomForItem(ownedItem.item.code),
                    x = getDefaultXForItem(ownedItem.item.code),
                    y = getDefaultYForItem(ownedItem.item.code)
                )

                when (result) {
                    is PlaceItemResult.Success -> {
                        val updatedOwnedItems = itemRepository.getOwnedItems()

                        _uiState.value = _uiState.value.copy(
                            sections = updatedOwnedItems.toItemSections(),
                            successMessage = "Placed ${ownedItem.item.name}!",
                            errorMessage = null
                        )
                    }

                    PlaceItemResult.ItemNotFound -> {
                        _uiState.value = _uiState.value.copy(errorMessage = "Item not found")
                    }

                    PlaceItemResult.ItemNotOwned -> {
                        _uiState.value = _uiState.value.copy(errorMessage = "You do not own this item")
                    }

                    PlaceItemResult.ItemNotPlaceable -> {
                        _uiState.value = _uiState.value.copy(errorMessage = "This item cannot be placed")
                    }

                    PlaceItemResult.FailedToRemoveFromInventory -> {
                        _uiState.value = _uiState.value.copy(errorMessage = "Could not remove item from inventory")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to place item"
                )
            }
        }
    }

    private fun List<OwnedItem>.toItemSections(): List<InventorySectionUi> {
        return groupBy { it.item.type }
            .map { (type, items) ->
                InventorySectionUi(
                    title = when (type) {
                        ItemType.FOOD -> "Food"
                        ItemType.FURNITURE -> "Furniture"
                        ItemType.TOY -> "Toys"
                        ItemType.HYGIENE -> "Hygiene"
                        ItemType.BOWL -> "Bowls"
                    },
                    items = items
                )
            }
    }

    private fun getDefaultRoomForItem(itemCode: ItemCode): RoomType {
        return when (itemCode) {
            ItemCode.BOWL -> RoomType.KITCHEN
            ItemCode.LITTER_BOX -> RoomType.BATHROOM
            ItemCode.CAT_BED -> RoomType.LIVINGROOM
            ItemCode.SCRATCHING_POST -> RoomType.LIVINGROOM
            ItemCode.BALL -> RoomType.LIVINGROOM
            ItemCode.MOUSE -> RoomType.LIVINGROOM
            ItemCode.WAND -> RoomType.LIVINGROOM

            else -> RoomType.LIVINGROOM
        }
    }

    private fun getDefaultXForItem(itemCode: ItemCode): Int {
        return when (itemCode) {
            ItemCode.BOWL -> 620
            ItemCode.LITTER_BOX -> 620
            ItemCode.CAT_BED -> 260
            ItemCode.SCRATCHING_POST -> 720
            ItemCode.BALL -> 420
            ItemCode.MOUSE -> 500
            ItemCode.WAND -> 580
            else -> 400
        }
    }

    private fun getDefaultYForItem(itemCode: ItemCode): Int {
        return when (itemCode) {
            ItemCode.BOWL -> 360
            ItemCode.LITTER_BOX -> 360
            ItemCode.CAT_BED -> 360
            ItemCode.SCRATCHING_POST -> 280
            ItemCode.BALL -> 430
            ItemCode.MOUSE -> 430
            ItemCode.WAND -> 430
            else -> 360
        }
    }
}