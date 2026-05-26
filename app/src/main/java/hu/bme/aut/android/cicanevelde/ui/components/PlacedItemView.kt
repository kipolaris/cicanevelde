package hu.bme.aut.android.cicanevelde.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.cicanevelde.R
import hu.bme.aut.android.cicanevelde.domain.model.enums.ItemCode

@Composable
fun PlacedItemView(
    itemUi: PlacedItemUi,
    onClick: () -> Unit
) {
    val image = when (itemUi.placedItem.item.code) {
        ItemCode.BOWL -> R.drawable.bowl_empty
        ItemCode.LITTER_BOX -> R.drawable.litterbox_clean
        ItemCode.BALL -> R.drawable.ball
        ItemCode.MOUSE -> R.drawable.mouse
        ItemCode.WAND -> R.drawable.wand
        else -> return
    }

    Image(
        painter = painterResource(image),
        contentDescription = null,
        modifier = Modifier
            .offset(x = itemUi.placedItem.positionX.dp, y = itemUi.placedItem.positionY.dp)
            .size(96.dp)
            .clickable(enabled = itemUi.interactionType != null) {onClick()}
    )
}