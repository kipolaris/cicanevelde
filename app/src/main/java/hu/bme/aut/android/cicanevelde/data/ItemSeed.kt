package hu.bme.aut.android.cicanevelde.data

import hu.bme.aut.android.cicanevelde.data.entity.ItemEntity
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemType

object ItemSeed {
    val defaultItems = listOf(
        ItemEntity(code = ItemCode.DRY_FOOD, name = "Dry food", price = 10, type = ItemType.FOOD, isPlaceable = false),
        ItemEntity(code = ItemCode.CANNED_FOOD, name = "Canned food", price = 15, type = ItemType.FOOD, isPlaceable = false),
        ItemEntity(code = ItemCode.TREATS, name = "Treats", price = 5, type = ItemType.FOOD, isPlaceable = false),
        ItemEntity(code = ItemCode.BOWL, name = "Bowl", price = 10, type = ItemType.BOWL, isPlaceable = true),
        ItemEntity(code = ItemCode.CAT_BED, name = "Cat bed", price = 100, type = ItemType.FURNITURE, isPlaceable = true),
        ItemEntity(code = ItemCode.SCRATCHING_POST, name = "Scratching post", price = 200, type = ItemType.FURNITURE, isPlaceable = true),
        ItemEntity(code = ItemCode.BALL, name = "Ball", price = 10, type = ItemType.TOY, isPlaceable = true),
        ItemEntity(code = ItemCode.MOUSE, name = "Mouse", price = 15, type = ItemType.TOY, isPlaceable = true),
        ItemEntity(code = ItemCode.WAND, name = "Wand", price = 25, type = ItemType.TOY, isPlaceable = true),
        ItemEntity(code = ItemCode.BRUSH, name = "Brush", price = 20, type = ItemType.HYGIENE, isPlaceable = false ),
        ItemEntity(code = ItemCode.CAT_SHAMPOO, name = "Cat shampoo", price = 35, type = ItemType.HYGIENE, isPlaceable = false),
        ItemEntity(code = ItemCode.LITTER_BOX, name = "Litter box", price = 50, type = ItemType.FURNITURE, isPlaceable = true),
        ItemEntity(code = ItemCode.LITTER, name = "Litter", price = 20, type = ItemType.HYGIENE, isPlaceable = false)
    )
}