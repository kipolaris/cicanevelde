package hu.bme.aut.android.cicanevelde.domain.mappers

import hu.bme.aut.android.cicanevelde.data.entity.BowlStateEntity
import hu.bme.aut.android.cicanevelde.data.entity.GameStateEntity
import hu.bme.aut.android.cicanevelde.data.entity.LitterStateEntity
import hu.bme.aut.android.cicanevelde.domain.model.BowlState
import hu.bme.aut.android.cicanevelde.domain.model.GameState
import hu.bme.aut.android.cicanevelde.domain.model.Item
import hu.bme.aut.android.cicanevelde.domain.model.LitterState
import hu.bme.aut.android.cicanevelde.domain.model.PlacedItem

fun GameStateEntity.toDomain() = GameState(
    catCoins = this.catCoins,
    isInitialized = this.isInitialized,
    selectedCatId = this.selectedCatId,
    currentRoom = this.currentRoom
)

fun GameState.toEntity() = GameStateEntity(
    catCoins = this.catCoins,
    isInitialized = this.isInitialized,
    selectedCatId = this.selectedCatId,
    currentRoom = this.currentRoom
)

fun BowlStateEntity.toDomain(placedItem: PlacedItem, food: Item?) = BowlState(
    id = id,
    placedItem = placedItem,
    food = food
)

fun BowlState.toEntity() = BowlStateEntity(
    id = id,
    placedItemId = placedItem.id,
    foodItemId = food?.id
)

fun LitterStateEntity.toDomain(placedItem: PlacedItem) = LitterState(
    id = id,
    placedItem = placedItem,
    isFull = isFull
)

fun LitterState.toEntity() = LitterStateEntity(
    id = id,
    placedItemId = placedItem.id,
    isFull = isFull
)