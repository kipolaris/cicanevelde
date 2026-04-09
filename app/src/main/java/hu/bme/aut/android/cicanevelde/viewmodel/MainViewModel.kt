package hu.bme.aut.android.cicanevelde.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.cicanevelde.data.repository.CatRepository
import hu.bme.aut.android.cicanevelde.domain.usecase.GameInitializer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val gameInitializer: GameInitializer,
    private val catRepository: CatRepository
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _needsStarterCat = MutableStateFlow(false)
    val needsStarterCat: StateFlow<Boolean> = _needsStarterCat

    init {
        viewModelScope.launch {
            try {
                gameInitializer()
                _needsStarterCat.value = !catRepository.hasAnyCat()
            } finally {
                _isLoading.value = false
            }
        }
    }
}