package hu.bme.aut.android.cicanevelde.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import hu.bme.aut.android.cicanevelde.R
import hu.bme.aut.android.cicanevelde.ui.components.InventoryItemButton
import hu.bme.aut.android.cicanevelde.ui.components.PinkButton
import hu.bme.aut.android.cicanevelde.viewmodel.InventoryViewModel

@Composable
fun InventoryScreen(
    viewModel: InventoryViewModel = hiltViewModel(),
    onHomeClick: () -> Unit = {},
    onStoreClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.creation),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PinkButton(
                text = "Home",
                onClick = onHomeClick
            )

            Text(
                text = "Inventory",
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            PinkButton(
                text = "Store",
                onClick = onStoreClick
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 48.dp, end = 96.dp, top = 150.dp, bottom = 56.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            uiState.sections.forEach { section ->
                item {
                    Text(
                        text = section.title,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(48.dp)
                    ) {
                        section.items.forEach { ownedItem ->
                            InventoryItemButton(
                                ownedItem = ownedItem,
                                onPlaceClick = {
                                    viewModel.placeItem(ownedItem)
                                }
                            )
                        }
                    }
                }
            }
        }

        uiState.successMessage?.let { message ->
            Text(
                text = message,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        uiState.errorMessage?.let { message ->
            Text(
                text = message,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}