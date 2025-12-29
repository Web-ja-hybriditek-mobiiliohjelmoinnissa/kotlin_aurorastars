package fi.antero.aurorastars.viewmodel.aurora

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fi.antero.aurorastars.data.model.aurora.AuroraData
import fi.antero.aurorastars.data.repository.AuroraRepository
import fi.antero.aurorastars.data.repository.AuroraRepositoryImpl
import fi.antero.aurorastars.data.source.aurora.AuroraRemoteDataSource
import fi.antero.aurorastars.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuroraViewModel : ViewModel() {

    private val repository: AuroraRepository = AuroraRepositoryImpl(
        remote = AuroraRemoteDataSource()
    )

    private val _uiState: MutableStateFlow<AuroraUiState> = MutableStateFlow(
        AuroraUiState(
            isLoading = false,
            data = null,
            error = null
        )
    )
    val uiState: StateFlow<AuroraUiState> = _uiState

    fun loadAurora(lat: Double): Unit {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result: Result<AuroraData> = repository.getAurora(lat)

            when (result) {
                is Result.Success -> {
                    _uiState.value = AuroraUiState(
                        isLoading = false,
                        data = result.data,
                        error = null
                    )
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }

                Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }
}
