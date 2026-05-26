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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.android.cicanevelde.domain.model.Item


@Composable
fun StoreItemButton(
    item: Item,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start
    ) {
        PinkButton(item.name, onClick = onClick)

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Price: ${item.price} CC",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}