package hu.bme.aut.android.cicanevelde.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import hu.bme.aut.android.cicanevelde.ui.screens.CreateCatScreen
import hu.bme.aut.android.cicanevelde.ui.screens.HomeScreen
import hu.bme.aut.android.cicanevelde.ui.screens.InventoryScreen
import hu.bme.aut.android.cicanevelde.ui.screens.StoreScreen
import hu.bme.aut.android.cicanevelde.viewmodel.MainViewModel

enum class AppScreen {
    HOME,
    STORE,
    INVENTORY
}

@Composable
fun AppRoot(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val isLoading by mainViewModel.isLoading.collectAsState()
    val needsStarterCat by mainViewModel.needsStarterCat.collectAsState()

    var currentScreen by remember {
        mutableStateOf(AppScreen.HOME)
    }

    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        needsStarterCat -> {
            CreateCatScreen(onCatCreated = { mainViewModel.refreshStartupState() })
        }

        else -> {
            when (currentScreen) {
                AppScreen.HOME -> {
                    HomeScreen(
                        onStoreClick = {
                            currentScreen = AppScreen.STORE
                        },
                        onInventoryClick = {
                            currentScreen = AppScreen.INVENTORY
                        }
                    )
                }

                AppScreen.STORE -> {
                    StoreScreen(
                        onHomeClick = {
                            currentScreen = AppScreen.HOME
                        },
                        onInventoryClick = {
                            currentScreen = AppScreen.INVENTORY
                        }
                    )
                }

                AppScreen.INVENTORY -> {
                    InventoryScreen(
                        onHomeClick = {
                            currentScreen = AppScreen.HOME
                        },
                        onStoreClick = {
                            currentScreen = AppScreen.STORE
                        }
                    )
                }
            }
        }
    }
}