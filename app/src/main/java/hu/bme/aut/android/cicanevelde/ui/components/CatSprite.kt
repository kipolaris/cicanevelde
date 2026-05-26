package hu.bme.aut.android.cicanevelde.ui.components

import androidx.compose.foundation.Image
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import hu.bme.aut.android.cicanevelde.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun CatSprite(modifier: Modifier) {
    val idleFrames = listOf(
        R.drawable.idle1,
        R.drawable.idle2,
        R.drawable.idle3,
        R.drawable.idle4,
        R.drawable.idle5,
        R.drawable.idle6,
        R.drawable.idle7,
        R.drawable.idle8
    )

    var frameIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(180L)
            frameIndex = (frameIndex + 1) % idleFrames.size
        }
    }

    Image(
        painter = painterResource(idleFrames[frameIndex]),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}