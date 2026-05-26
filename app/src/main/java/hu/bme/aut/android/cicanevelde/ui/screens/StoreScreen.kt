package hu.bme.aut.android.cicanevelde.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import hu.bme.aut.android.cicanevelde.R
import hu.bme.aut.android.cicanevelde.ui.components.PinkButton
import hu.bme.aut.android.cicanevelde.ui.components.StoreItemButton
import hu.bme.aut.android.cicanevelde.ui.theme.CicaneveldeTheme
import hu.bme.aut.android.cicanevelde.viewmodel.StoreViewModel

@Composable
fun StoreScreen(
    viewModel: StoreViewModel = hiltViewModel(),
    onHomeClick: () -> Unit = {},
    onInventoryClick: () -> Unit = {}
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

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Store",
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "Cat coins: ${uiState.catCoins}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            PinkButton(
                text = "Inventory",
                onClick = onInventoryClick
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 48.dp, end = 96.dp, top = 150.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            uiState.sections.forEach { section ->
                item {
                    Text(
                        text = section.title,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }

                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(48.dp)
                    ) {
                        section.items.forEach { item ->
                            StoreItemButton(
                                item = item,
                                onClick = {
                                    viewModel.buyItem(item)
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
                    .padding(bottom = 24.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        uiState.errorMessage?.let { message ->
            Text(
                text = message,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 900,
    heightDp = 500
)
@Composable
fun StoreScreenPreview() {
    CicaneveldeTheme {
        StoreScreen()
    }
}