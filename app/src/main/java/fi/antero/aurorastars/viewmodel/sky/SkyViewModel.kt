package fi.antero.aurorastars.viewmodel.sky

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fi.antero.aurorastars.util.Result
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SkyViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SkyUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            delay(800)
            _uiState.value = SkyUiState(
                Result.Success("Fake tähtikartta – kupoli näkymä")
            )
        }
    }
}
