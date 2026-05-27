package hu.bme.aut.android.cicanevelde.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.cicanevelde.data.repository.BowlRepository
import hu.bme.aut.android.cicanevelde.data.repository.CatRepository
import hu.bme.aut.android.cicanevelde.data.repository.GameStateRepository
import hu.bme.aut.android.cicanevelde.data.repository.ItemRepository
import hu.bme.aut.android.cicanevelde.data.repository.LitterRepository
import hu.bme.aut.android.cicanevelde.data.repository.PlacedItemRepository
import hu.bme.aut.android.cicanevelde.domain.model.Cat
import hu.bme.aut.android.cicanevelde.domain.model.PlacedItem
import hu.bme.aut.android.cicanevelde.domain.model.enums.CatActionType
import hu.bme.aut.android.cicanevelde.domain.model.enums.CatActivity
import hu.bme.aut.android.cicanevelde.domain.model.enums.InteractionType
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.model.enums.RoomType
import hu.bme.aut.android.cicanevelde.domain.result.CareActionResult
import hu.bme.aut.android.cicanevelde.domain.result.EatFoodResult
import hu.bme.aut.android.cicanevelde.domain.result.UseLitterResult
import hu.bme.aut.android.cicanevelde.domain.result.item.FillBowlResult
import hu.bme.aut.android.cicanevelde.domain.usecase.BatheCatUseCase
import hu.bme.aut.android.cicanevelde.domain.usecase.BrushCatUseCase
import hu.bme.aut.android.cicanevelde.domain.usecase.DecideCatBehaviorUseCase
import hu.bme.aut.android.cicanevelde.domain.usecase.EatFoodUseCase
import hu.bme.aut.android.cicanevelde.domain.usecase.GiveCatTreatUseCase
import hu.bme.aut.android.cicanevelde.domain.usecase.PetCatUseCase
import hu.bme.aut.android.cicanevelde.domain.usecase.PlayWithCatUseCase
import hu.bme.aut.android.cicanevelde.domain.usecase.SleepUseCase
import hu.bme.aut.android.cicanevelde.domain.usecase.UseLitterUseCase
import hu.bme.aut.android.cicanevelde.ui.components.CatActionUi
import hu.bme.aut.android.cicanevelde.viewmodel.uistate.HomeUiState
import hu.bme.aut.android.cicanevelde.ui.components.PlacedItemUi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val gameStateRepository: GameStateRepository,
    private val catRepository: CatRepository,
    private val placedItemRepository: PlacedItemRepository,
    private val bowlRepository: BowlRepository,
    private val litterRepository: LitterRepository,
    private val playWithCatUseCase: PlayWithCatUseCase,
    private val itemRepository: ItemRepository,
    private val petCatUseCase: PetCatUseCase,
    private val brushCatUseCase: BrushCatUseCase,
    private val batheCatUseCase: BatheCatUseCase,
    private val giveCatTreatUseCase: GiveCatTreatUseCase,
    private val decideCatBehaviorUseCase: DecideCatBehaviorUseCase,
    private val eatFoodUseCase: EatFoodUseCase,
    private val useLitterUseCase: UseLitterUseCase,
    private val sleepUseCase: SleepUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val rooms = listOf(
        RoomType.LIVINGROOM,
        RoomType.KITCHEN,
        RoomType.BATHROOM
    )

    private var isRefreshingCat = false
    private var isHandlingCatBehavior = false

    init {
        loadHome()
        startGameLoop()
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

                val catActions = loadCatActions()

                val (catRoom, catActivity) =
                    decideCatBehaviorUseCase(refreshedCat)
                val (catX, catY) = getCatPositionForActivity(catActivity)

                _uiState.value = HomeUiState(
                    isLoading = false,
                    cat = refreshedCat,
                    currentRoom = gameState.currentRoom,
                    catActions = catActions,
                    catRoom = catRoom,
                    catX = catX,
                    catY = catY,
                    catActivity = catActivity,
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

    private fun startGameLoop() {
        viewModelScope.launch {
            while (true) {
                delay(5_000L)
                refreshCat()
            }
        }
    }

    private fun selectRoom(room: RoomType) {
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
        if (isRefreshingCat) return
        val cat = _uiState.value.cat ?: return

        viewModelScope.launch {
            isRefreshingCat = true
            try {
                val refreshedCat = catRepository.refreshCatStats(cat)

                val catActions = loadCatActions()

                val (catRoom, catActivity) =
                    decideCatBehaviorUseCase(refreshedCat)
                val (catX, catY) = getCatPositionForActivity(catActivity)

                _uiState.value = _uiState.value.copy(
                    cat = refreshedCat,
                    catActions = catActions,
                    catRoom = catRoom,
                    catActivity = catActivity,
                    catX = catX,
                    catY = catY,
                    errorMessage = null
                )

                when (catActivity) {
                    CatActivity.EATING -> handleEating(refreshedCat)
                    CatActivity.USING_LITTER -> handleUsingLitter(refreshedCat)
                    CatActivity.SLEEPING -> handleSleeping(refreshedCat)
                    else -> Unit
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to refresh cat"
                )
            } finally {
                isRefreshingCat = false
            }
        }
    }

    private suspend fun loadPlacedItemUi(room: RoomType): List<PlacedItemUi> {
        val placedItems = placedItemRepository.getPlacedItemsInRoom(room)

        return placedItems.map { placedItem ->
            val bowlState = if (placedItem.item.code == ItemCode.BOWL) {
                bowlRepository.getBowlState(placedItem.id)
            } else {
                null
            }

            val litterState = if (placedItem.item.code == ItemCode.LITTER_BOX) {
                litterRepository.getLitterState(placedItem.id)
            } else {
                null
            }

            val isBowlFilled = bowlState?.food != null
            val isLitterFull = litterState?.isFull ?: false

            PlacedItemUi(
                placedItem = placedItem,
                interactionType = getInteractionType(placedItem),
                isBowlFilled = isBowlFilled,
                isLitterFull = isLitterFull
            )
        }
    }

    private fun getInteractionType(placedItem: PlacedItem): InteractionType? {
        return when (placedItem.item.code) {
            ItemCode.BOWL -> InteractionType.FILL_BOWL
            ItemCode.WAND -> InteractionType.PLAY
            ItemCode.BALL -> InteractionType.PLAY
            ItemCode.MOUSE -> InteractionType.PLAY
            ItemCode.LITTER_BOX -> InteractionType.CLEAN_LITTER
            else -> null
        }
    }

    fun onPlacedItemClicked(itemUi: PlacedItemUi) {
        when (itemUi.interactionType) {
            InteractionType.FILL_BOWL -> {
                fillBowl(itemUi)
            }

            InteractionType.CLEAN_LITTER -> {
                cleanLitter(itemUi)
            }

            InteractionType.PLAY -> {
                playWithCat(itemUi)
            }

            null -> Unit
        }
    }

    private fun fillBowl(itemUi: PlacedItemUi) {
        viewModelScope.launch {
            try {
                val result = bowlRepository.fillBowl(
                    placedItemId = itemUi.placedItem.id,
                    foodCode = ItemCode.DRY_FOOD
                )

                when (result) {
                    FillBowlResult.Success -> {
                        _uiState.value = _uiState.value.copy(
                            placedItems = loadPlacedItemUi(_uiState.value.currentRoom),
                            errorMessage = null
                        )

                        refreshCat()
                    }

                    FillBowlResult.BowlAlreadyFull -> {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "This bowl is already full."
                        )
                    }

                    FillBowlResult.BowlNotFound -> {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "Bowl not found."
                        )
                    }

                    FillBowlResult.NoFoodAvailable -> {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "You do not have any food."
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to fill bowl"
                )
            }
        }
    }

    private fun cleanLitter(itemUi: PlacedItemUi) {
        viewModelScope.launch {
            try {
                litterRepository.cleanLitter(itemUi.placedItem.id)

                val updatedPlacedItems = loadPlacedItemUi(_uiState.value.currentRoom)

                _uiState.value = _uiState.value.copy(
                    placedItems = updatedPlacedItems,
                    errorMessage = null
                )

                refreshCat()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to clean litter"
                )
            }
        }
    }

    private fun playWithCat(placedItemUi: PlacedItemUi) {
        val cat = _uiState.value.cat ?: return

        viewModelScope.launch {
            try {
                val result = playWithCatUseCase(cat, placedItemUi.placedItem)

                when (result) {
                    is CareActionResult.Success -> {
                        val refreshedCat = catRepository.getCatById(cat.id)

                        _uiState.value = _uiState.value.copy(
                            cat = refreshedCat,
                            errorMessage = null
                        )
                    }

                    CareActionResult.NotEnoughEnergy -> {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "Your cat is too tired to play."
                        )
                    }

                    CareActionResult.NoItemAvailable -> {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = "You need a toy to play."
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to play with cat"
                )
            }
        }
    }

    fun onCatClicked() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isCatStatsPopupVisible = !_uiState.value.isCatStatsPopupVisible,
                isCatActionMenuVisible = !_uiState.value.isCatActionMenuVisible,
                catActions = loadCatActions()
            )
        }
    }

    fun onStatsPopupClicked() {
        _uiState.value = _uiState.value.copy(
            isCatStatsPopupVisible = false,
            isCatActionMenuVisible = false
        )
    }

    private suspend fun loadCatActions(): List<CatActionUi> {
        val actions = mutableListOf(
            CatActionUi("Pet Cat", CatActionType.PET)
        )

        if (itemRepository.getOwnedItemByCode(ItemCode.BRUSH) != null) {
            actions.add(CatActionUi("Brush Cat", CatActionType.BRUSH))
        }

        if (itemRepository.getOwnedItemByCode(ItemCode.CAT_SHAMPOO) != null) {
            actions.add(CatActionUi("Bathe Cat", CatActionType.BATHE))
        }

        if (itemRepository.getOwnedItemByCode(ItemCode.TREATS) != null) {
            actions.add(CatActionUi("Give Treat", CatActionType.GIVE_TREAT))
        }

        return actions
    }

    fun onCatActionClicked(action: CatActionType) {
        val cat = _uiState.value.cat ?: return

        viewModelScope.launch {
            try {
                val result = when (action) {
                    CatActionType.PET -> petCatUseCase(cat)
                    CatActionType.BRUSH -> brushCatUseCase(cat)
                    CatActionType.BATHE -> batheCatUseCase(cat)
                    CatActionType.GIVE_TREAT -> giveCatTreatUseCase(cat)
                }

                handleCareActionResult(result, cat.id)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Action failed"
                )
            }
        }
    }

    private suspend fun handleCareActionResult(
        result: CareActionResult,
        catId: Long
    ) {
        when (result) {
            is CareActionResult.Success -> {
                val updatedCat = catRepository.getCatById(catId)

                _uiState.value = _uiState.value.copy(
                    cat = updatedCat,
                    catActions = loadCatActions(),
                    errorMessage = null
                )
            }

            CareActionResult.NotEnoughEnergy -> {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Your cat is too tired."
                )
            }

            CareActionResult.NoItemAvailable -> {
                _uiState.value = _uiState.value.copy(
                    catActions = loadCatActions(),
                    errorMessage = "You do not have the required item."
                )
            }
        }
    }

    private suspend fun getCatPositionForActivity(
        catActivity: CatActivity
    ): Pair<Int, Int> {
        return when (catActivity) {
            CatActivity.EATING -> {
                val bowl = bowlRepository.getRandomFilledBowl()

                if (bowl != null) {
                    val bowlX = bowl.placedItem.positionX
                    val bowlY = bowl.placedItem.positionY

                    bowlX + 50 to bowlY - 220
                } else {
                    860 to 650
                }
            }

            CatActivity.SLEEPING -> {
                160 to 620
            }

            CatActivity.USING_LITTER -> {
                val litterBox = litterRepository.getFirstCleanLitter()

                if (litterBox != null) {
                    val litterBoxX = litterBox.placedItem.positionX
                    val litterBoxY = litterBox.placedItem.positionY

                    litterBoxX to litterBoxY - 150
                } else {
                    1420 to 620
                }
            }

            else -> {
                860 to 550
            }
        }
    }

    private fun handleEating(cat: Cat) {
        if (isHandlingCatBehavior) return

        viewModelScope.launch {
            isHandlingCatBehavior = true

            try {
                delay(3000L)

                val result = eatFoodUseCase(cat)

                when (result) {
                    EatFoodResult.Success -> {
                        val updatedCat = catRepository.getCatById(cat.id)

                        if (updatedCat != null) {
                            val (newRoom, newActivity) =
                                decideCatBehaviorUseCase(updatedCat)

                            val (newX, newY) =
                                getCatPositionForActivity(newActivity)

                            _uiState.value = _uiState.value.copy(
                                cat = updatedCat,
                                catRoom = newRoom,
                                catActivity = newActivity,
                                catX = newX,
                                catY = newY,
                                placedItems = loadPlacedItemUi(_uiState.value.currentRoom),
                                errorMessage = null
                            )
                        }
                    }

                    EatFoodResult.NoFoodAvailable -> {
                        _uiState.value = _uiState.value.copy(
                            catActivity = CatActivity.IDLE,
                            errorMessage = "No food available."
                        )
                    }

                    EatFoodResult.NotHungryEnough -> {
                        _uiState.value = _uiState.value.copy(
                            catActivity = CatActivity.IDLE
                        )
                    }
                }
            } finally {
                isHandlingCatBehavior = false
            }
        }
    }

    private fun handleUsingLitter(cat: Cat) {
        if (isHandlingCatBehavior) return

        viewModelScope.launch {
            isHandlingCatBehavior = true

            try {
                delay(3000L)

                val result = useLitterUseCase(cat)

                when (result) {
                    UseLitterResult.Success -> {
                        val updatedCat = catRepository.getCatById(cat.id)

                        if (updatedCat != null) {
                            val (newRoom, newActivity) =
                                decideCatBehaviorUseCase(updatedCat)

                            val (newX, newY) =
                                getCatPositionForActivity(newActivity)

                            _uiState.value = _uiState.value.copy(
                                cat = updatedCat,
                                catRoom = newRoom,
                                catActivity = newActivity,
                                catX = newX,
                                catY = newY,
                                placedItems = loadPlacedItemUi(_uiState.value.currentRoom),
                                errorMessage = null
                            )
                        }
                    }
                    UseLitterResult.NoLitterAvailable -> {
                        _uiState.value = _uiState.value.copy(
                            catActivity = CatActivity.IDLE,
                            errorMessage = "No litter available."
                        )
                    }
                    UseLitterResult.NoNeedTo -> {
                        _uiState.value = _uiState.value.copy(
                            catActivity = CatActivity.IDLE
                        )
                    }
                }
            } finally {
                isHandlingCatBehavior = false
            }
        }
    }

    private fun handleSleeping(cat: Cat) {
        if (isHandlingCatBehavior) return

        viewModelScope.launch {
            isHandlingCatBehavior = true

            try {
                sleepUseCase(cat)

                val updatedCat = catRepository.getCatById(cat.id)

                if (updatedCat != null) {
                    val (newRoom, newActivity) =
                        decideCatBehaviorUseCase(updatedCat)

                    val (newX, newY) =
                        getCatPositionForActivity(newActivity)

                    _uiState.value = _uiState.value.copy(
                        cat = updatedCat,
                        catRoom = newRoom,
                        catActivity = newActivity,
                        catX = newX,
                        catY = newY,
                        errorMessage = null
                    )
                }
            } finally {
                isHandlingCatBehavior = false
            }
        }
    }
}