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
import hu.bme.aut.android.cicanevelde.domain.model.enums.CatActivity
import kotlinx.coroutines.delay

@Composable
fun CatSprite(
    activity: CatActivity,
    modifier: Modifier
) {
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

    val eatingFrames = listOf(
        R.drawable.eating1,
        R.drawable.eating2,
        R.drawable.eating3,
        R.drawable.eating4,
        R.drawable.eating5,
        R.drawable.eating6,
        R.drawable.eating7
    )

    val sleepingFrames = listOf(
        R.drawable.sleeping1,
        R.drawable.sleeping2,
        R.drawable.sleeping3,
        R.drawable.sleeping4,
        R.drawable.sleeping5,
        R.drawable.sleeping6,
        R.drawable.sleeping7,
        R.drawable.sleeping8
    )

    val frames = when (activity) {
        CatActivity.IDLE -> idleFrames
        CatActivity.EATING -> eatingFrames
        CatActivity.SLEEPING -> sleepingFrames
        CatActivity.USING_LITTER -> idleFrames
    }


    var frameIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(activity) {
        frameIndex = 0

        while (true) {
            delay(180L)
            frameIndex = (frameIndex + 1) % idleFrames.size
        }
    }

    Image(
        painter = painterResource(frames[frameIndex.coerceAtMost(frames.lastIndex)]),
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}