package fi.antero.aurorastars.viewmodel.aurora

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fi.antero.aurorastars.util.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuroraViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuroraUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadFakeAurora()
    }

    private fun loadFakeAurora() {
        viewModelScope.launch {
            delay(700)
            _uiState.value = AuroraUiState(
                Result.Success("Fake Kp 4.3 – Kohtalainen mahdollisuus")
            )
        }
    }
}
