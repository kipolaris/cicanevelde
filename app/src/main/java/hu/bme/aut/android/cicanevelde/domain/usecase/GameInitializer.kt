package hu.bme.aut.android.cicanevelde.domain.usecase

import hu.bme.aut.android.cicanevelde.data.repository.GameStateRepository
import hu.bme.aut.android.cicanevelde.data.repository.ItemRepository
import javax.inject.Inject

class GameInitializer @Inject constructor(
    private val gameStateRepository: GameStateRepository,
    private val itemRepository: ItemRepository
) {
    suspend operator fun invoke() {
        val gameState = gameStateRepository.getGameState()

        if (gameState?.isInitialized == true) return

        gameStateRepository.initializeGameState()
        itemRepository.initializeDefaultItems()
        gameStateRepository.markInitialized()
    }
}