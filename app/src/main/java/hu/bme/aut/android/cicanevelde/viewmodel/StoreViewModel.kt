package hu.bme.aut.android.cicanevelde.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.cicanevelde.data.repository.GameStateRepository
import hu.bme.aut.android.cicanevelde.data.repository.ItemRepository
import hu.bme.aut.android.cicanevelde.domain.model.Item
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemType
import hu.bme.aut.android.cicanevelde.domain.result.item.BuyItemResult
import hu.bme.aut.android.cicanevelde.ui.components.StoreSectionUi
import hu.bme.aut.android.cicanevelde.viewmodel.uistate.StoreUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val gameStateRepository: GameStateRepository,
    private val itemRepository: ItemRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(StoreUiState())
    val uiState: StateFlow<StoreUiState> = _uiState

    init {
        loadStore()
    }

    fun loadStore() {
        viewModelScope.launch {
            try {
                val gameState = gameStateRepository.getGameState()
                val items = itemRepository.getAllItems()

                _uiState.value = StoreUiState(
                    catCoins = gameState?.catCoins ?: 0,
                    sections = items.toItemSections(),
                    errorMessage = null,
                    successMessage = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to load store"
                )
            }
        }
    }

    fun buyItem(item: Item) {
        viewModelScope.launch {
            val result = itemRepository.buyItem(item.code)

            when (result) {
                BuyItemResult.Success -> {
                    val updatedGameState = gameStateRepository.getGameState()

                    _uiState.value = _uiState.value.copy(
                        catCoins = updatedGameState?.catCoins ?: 0,
                        successMessage = "Bought ${item.name}!",
                        errorMessage = null
                    )
                }

                BuyItemResult.NotEnoughCoins -> {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Not enough cat coins!",
                        successMessage = null
                    )
                }

                BuyItemResult.ItemNotFound -> {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Item not found!",
                        successMessage = null
                    )
                }

                BuyItemResult.GameStateNotFound -> {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Game state not found!",
                        successMessage = null
                    )
                }
            }
        }
    }

    private fun List<Item>.toItemSections(): List<StoreSectionUi> {
        return groupBy { it.type }
            .map { (type, items) ->
                StoreSectionUi(
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
}