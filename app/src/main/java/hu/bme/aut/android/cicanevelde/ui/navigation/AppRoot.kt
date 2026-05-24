package hu.bme.aut.android.cicanevelde.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import hu.bme.aut.android.cicanevelde.ui.screens.CreateCatScreen
import hu.bme.aut.android.cicanevelde.ui.screens.HomeScreen
import hu.bme.aut.android.cicanevelde.viewmodel.MainViewModel

@Composable
fun AppRoot(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val isLoading by mainViewModel.isLoading.collectAsState()
    val needsStarterCat by mainViewModel.needsStarterCat.collectAsState()

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
            HomeScreen()
        }
    }
}