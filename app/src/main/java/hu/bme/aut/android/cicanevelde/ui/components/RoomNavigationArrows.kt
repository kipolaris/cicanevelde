package hu.bme.aut.android.cicanevelde.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.cicanevelde.R

@Composable
fun RoomNavigationArrows(
    onPreviousRoom: () -> Unit,
    onNextRoom: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        Image(
            painter = painterResource(R.drawable.left_arrow_pink),
            contentDescription = "Previous room",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .size(72.dp)
                .clickable {
                    onPreviousRoom()
                }
        )

        Image(
            painter = painterResource(R.drawable.right_arrow_pink),
            contentDescription = "Next room",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
                .size(72.dp)
                .clickable {
                    onNextRoom()
                }
        )
    }
}