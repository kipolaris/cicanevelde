package hu.bme.aut.android.cicanevelde.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.cicanevelde.data.repository.CatRepository
import hu.bme.aut.android.cicanevelde.domain.model.enums.Gender
import hu.bme.aut.android.cicanevelde.domain.model.enums.Pattern
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCatViewModel @Inject constructor(
    private val catRepository: CatRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateCatUiState())
    val uiState: StateFlow<CreateCatUiState> = _uiState

    fun onNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun onGenderSelected(gender: Gender) {
        _uiState.value = _uiState.value.copy(selectedGender = gender)
    }

    fun onPatternSelected(pattern: Pattern) {
        _uiState.value = _uiState.value.copy(selectedPattern = pattern)
    }

    fun createCat() {
        val state = _uiState.value
        val gender = state.selectedGender
        val pattern = state.selectedPattern

        if (state.name.isBlank()) {
            _uiState.value = state.copy(errorMessage = "Please enter a name!")
            return
        }

        if (gender == null) {
            _uiState.value = state.copy(errorMessage = "Please select a gender!")
            return
        }

        if (pattern == null) {
            _uiState.value = state.copy(errorMessage = "Please select a pattern!")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true, errorMessage = null)

            try {
                catRepository.createCat(
                    name = state.name.trim(),
                    gender = gender,
                    pattern = pattern
                )

                _uiState.value = _uiState.value.copy(isSaving = false, creationComplete = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSaving = false,
                    errorMessage = e.message ?: "Failed to create cat"
                )
            }
        }
    }
}