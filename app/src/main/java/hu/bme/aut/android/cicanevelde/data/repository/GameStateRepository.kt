package hu.bme.aut.android.cicanevelde.data.repository

import hu.bme.aut.android.cicanevelde.data.dao.GameStateDao
import hu.bme.aut.android.cicanevelde.data.entity.GameStateEntity
import hu.bme.aut.android.cicanevelde.domain.mappers.toDomain
import hu.bme.aut.android.cicanevelde.domain.model.GameState
import hu.bme.aut.android.cicanevelde.domain.model.enums.RoomType
import kotlinx.coroutines.flow.Flow

class GameStateRepository(
    private val gameStateDao: GameStateDao
) {
    fun observeGameState(): Flow<GameStateEntity?> = gameStateDao.observeGameState()

    suspend fun getGameState(): GameState? = gameStateDao.getGameState()?.toDomain()

    suspend fun initializeGameState() {
        if (gameStateDao.getGameState() != null) return

        gameStateDao.insertGameState(
            GameStateEntity(
                id = 0,
                catCoins = 100,
                isInitialized = false,
                selectedCatId = 0,
                currentRoom = RoomType.LIVINGROOM
            )
        )
    }

    suspend fun addCatCoins(amount: Int) {
        if (amount <= 0) return

        val gameState = gameStateDao.getGameState() ?: return

        gameStateDao.updateGameState(
            gameState.copy(catCoins = gameState.catCoins + amount)
        )
    }

    suspend fun spendCatCoins(amount: Int): Boolean {
        if (amount <= 0) return false

        val gameState = gameStateDao.getGameState() ?: return false

        if (gameState.catCoins < amount) return false

        gameStateDao.updateGameState(
            gameState.copy(catCoins = gameState.catCoins - amount)
        )

        return true
    }

    suspend fun setSelectedCat(catId: Long) {
        val gameState = gameStateDao.getGameState() ?: return

        gameStateDao.updateGameState(
            gameState.copy(selectedCatId = catId)
        )
    }

    suspend fun setCurrentRoom(room: RoomType) {
        val gameState = gameStateDao.getGameState() ?: return

        gameStateDao.updateGameState(
            gameState.copy(currentRoom = room)
        )
    }

    suspend fun markInitialized() {
        val gameState = gameStateDao.getGameState() ?: return

        gameStateDao.updateGameState(
            gameState.copy(isInitialized = true)
        )
    }
}