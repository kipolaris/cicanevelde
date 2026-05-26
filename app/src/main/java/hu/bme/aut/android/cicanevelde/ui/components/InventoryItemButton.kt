package hu.bme.aut.android.cicanevelde.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.cicanevelde.R
import hu.bme.aut.android.cicanevelde.domain.model.OwnedItem

@Composable
fun InventoryItemButton(
    ownedItem: OwnedItem,
    onPlaceClick: () -> Unit
) {
    val isPlaceable = ownedItem.item.isPlaceable

    val background = if (isPlaceable) {
        R.drawable.button_pink
    } else {
        R.drawable.button_light_pink
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .width(150.dp)
                .height(89.dp)
                .clickable(enabled = isPlaceable) {
                    onPlaceClick()
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(background),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            Text(
                text = ownedItem.item.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Quantity: ${ownedItem.quantity}",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}