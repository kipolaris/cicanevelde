package hu.bme.aut.android.cicanevelde.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.cicanevelde.R
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode

@Composable
fun PlacedItemView(
    itemUi: PlacedItemUi,
    scaleX: Float,
    scaleY: Float,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val image = when (itemUi.placedItem.item.code) {
        ItemCode.BOWL -> {
            if (itemUi.isBowlFilled) {
                R.drawable.bowl_full
            } else {
                R.drawable.bowl_empty
            }
        }
        ItemCode.LITTER_BOX -> {
            if (itemUi.isLitterFull) {
                R.drawable.litterbox_dirty
            } else {
                R.drawable.litterbox_clean
            }
        }
        ItemCode.CAT_BED -> R.drawable.cat_bed
        ItemCode.SCRATCHING_POST -> R.drawable.scratching_post
        ItemCode.BALL -> R.drawable.ball
        ItemCode.MOUSE -> R.drawable.mouse
        ItemCode.WAND -> R.drawable.wand
        else -> return
    }

    val size = when (itemUi.placedItem.item.code) {
        ItemCode.LITTER_BOX -> 325 to 325
        ItemCode.BOWL -> 165 to 165
        ItemCode.CAT_BED -> 300 to 201
        ItemCode.SCRATCHING_POST -> 315 to 315

        ItemCode.BALL -> 90 to 90
        ItemCode.MOUSE -> 90 to 90
        ItemCode.WAND -> 120 to 120

        else -> 120 to 120
    }

    val uniformScale = minOf(scaleX, scaleY)

    val itemModifier = modifier
        .offset(
            x = scaledX(itemUi.placedItem.positionX, scaleX),
            y = scaledY(itemUi.placedItem.positionY, scaleY)
        )
        .size(
            width = scaledWidth(size.first, uniformScale),
            height = scaledHeight(size.second, uniformScale)
        )

    Image(
        painter = painterResource(image),
        contentDescription = itemUi.placedItem.item.name,
        modifier = if (itemUi.interactionType != null) {
            itemModifier.clickable { onClick() }
        } else {
            itemModifier
        },
        contentScale = ContentScale.Fit
    )
}

private fun scaledX(x: Int, scaleX: Float): Dp {
    return (x * scaleX).dp
}

private fun scaledY(y: Int, scaleY: Float): Dp {
    return (y * scaleY).dp
}

private fun scaledWidth(width: Int, scaleX: Float): Dp {
    return (width * scaleX).dp
}

private fun scaledHeight(height: Int, scaleY: Float): Dp {
    return (height * scaleY).dp
}

