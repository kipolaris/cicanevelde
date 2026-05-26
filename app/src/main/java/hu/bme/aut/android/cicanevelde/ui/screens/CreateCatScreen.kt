package hu.bme.aut.android.cicanevelde.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import hu.bme.aut.android.cicanevelde.R
import hu.bme.aut.android.cicanevelde.domain.model.enums.Gender
import hu.bme.aut.android.cicanevelde.viewmodel.CreateCatViewModel

@Composable
fun CreateCatScreen(
    viewModel: CreateCatViewModel = hiltViewModel(),
    onCatCreated: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.creationComplete) {
        if (uiState.creationComplete) {
            onCatCreated()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.creation),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Adopt your first cat",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.name,
                onValueChange = viewModel::onNameChanged,
                label = { Text("Cat name") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Gender")

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Gender.entries.forEach { gender ->
                    Button(
                        onClick = { viewModel.onGenderSelected(gender) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (uiState.selectedGender == gender) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.secondary
                            }
                        )
                    ) {
                        Text(gender.name)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Your starter cat will be orange.")

            Spacer(modifier = Modifier.height(24.dp))

            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Button(
                onClick = viewModel::createCat,
                enabled = !uiState.isSaving
            ) {
                Text(
                    if (uiState.isSaving) "Adopting..." else "Adopt cat"
                )
            }
        }
    }
}