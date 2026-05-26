package hu.bme.aut.android.cicanevelde.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import hu.bme.aut.android.cicanevelde.R
import hu.bme.aut.android.cicanevelde.domain.model.enums.RoomType

@Composable
fun RoomBackground(room: RoomType) {
    val background = when (room) {
        RoomType.LIVINGROOM -> R.drawable.livingroom
        RoomType.KITCHEN -> R.drawable.kitchen
        RoomType.BATHROOM -> R.drawable.bathroom
    }

    Image(
        painter = painterResource(background),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.FillBounds
    )
}