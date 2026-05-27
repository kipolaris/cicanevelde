package hu.bme.aut.android.cicanevelde.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import hu.bme.aut.android.cicanevelde.ui.components.CatActionMenu
import hu.bme.aut.android.cicanevelde.ui.components.CatSprite
import hu.bme.aut.android.cicanevelde.ui.components.CatStatsPopUp
import hu.bme.aut.android.cicanevelde.ui.components.PinkButton
import hu.bme.aut.android.cicanevelde.ui.components.PlacedItemView
import hu.bme.aut.android.cicanevelde.ui.components.RoomBackground
import hu.bme.aut.android.cicanevelde.ui.components.RoomNavigationArrows
import hu.bme.aut.android.cicanevelde.viewmodel.HomeViewModel

private const val DESIGN_WIDTH = 1920f
private const val DESIGN_HEIGHT = 1080f

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onStoreClick: () -> Unit = {},
    onInventoryClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadHome()
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val scaleX = maxWidth.value / DESIGN_WIDTH
        val scaleY = maxHeight.value / DESIGN_HEIGHT

        RoomBackground(uiState.currentRoom)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PinkButton(
                text = "Store",
                onClick = onStoreClick
            )

            PinkButton(
                text = "Inventory",
                onClick = onInventoryClick
            )
        }

        uiState.placedItems.forEach { itemUi ->
            PlacedItemView(
                itemUi = itemUi,
                scaleX = scaleX,
                scaleY = scaleY,
                onClick = {
                    viewModel.onPlacedItemClicked(itemUi)
                }
            )
        }

        if (uiState.currentRoom == uiState.catRoom && uiState.cat != null) {
            CatSprite(
                activity = uiState.catActivity,
                modifier = Modifier
                    .size(130.dp)
                    .offset(
                        x = (uiState.catX * scaleX).dp,
                        y = (uiState.catY * scaleY).dp
                    )
                    .clickable { viewModel.onCatClicked() }
            )
        }

        RoomNavigationArrows(
            onPreviousRoom = viewModel::goToPreviousRoom,
            onNextRoom = viewModel::goToNextRoom
        )

        if (uiState.isCatStatsPopupVisible && uiState.cat != null) {
            CatStatsPopUp(
                cat = uiState.cat!!,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 24.dp, bottom = 24.dp)
                    .clickable() { viewModel.onStatsPopupClicked() }
            )
        }

        if (uiState.isCatActionMenuVisible && uiState.cat != null) {
            CatActionMenu(
                actions = uiState.catActions,
                onActionClick = viewModel::onCatActionClicked,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 24.dp, bottom = 24.dp)
            )
        }
    }
}

