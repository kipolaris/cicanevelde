package hu.bme.aut.android.cicanevelde.domain.mappers

import hu.bme.aut.android.cicanevelde.data.entity.ItemEntity
import hu.bme.aut.android.cicanevelde.data.entity.OwnedItemEntity
import hu.bme.aut.android.cicanevelde.data.entity.PlacedItemEntity
import hu.bme.aut.android.cicanevelde.domain.model.Item
import hu.bme.aut.android.cicanevelde.domain.model.OwnedItem
import hu.bme.aut.android.cicanevelde.domain.model.PlacedItem

fun ItemEntity.toDomain() = Item(
    id = this.id,
    name = this.name,
    code = this.code,
    price = this.price,
    type = this.type,
    isPlaceable = this.isPlaceable
)

fun Item.toEntity() = ItemEntity(
    id = this.id,
    name = this.name,
    code = this.code,
    price = this.price,
    type = this.type,
    isPlaceable = this.isPlaceable
)

fun OwnedItemEntity.toDomain(item: Item) = OwnedItem(
    id = this.id,
    item = item,
    quantity = this.quantity
)

fun OwnedItem.toEntity() = OwnedItemEntity(
    id = this.id,
    itemId = this.item.id,
    quantity = this.quantity
)

fun PlacedItemEntity.toDomain(item: Item) = PlacedItem(
    id = this.id,
    item = item,
    room = this.room,
    positionX = this.positionX,
    positionY = this.positionY
)

fun PlacedItem.toEntity() = PlacedItemEntity(
    id = this.id,
    itemId = this.item.id,
    room = this.room,
    positionX = this.positionX,
    positionY = this.positionY
)