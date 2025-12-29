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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuroraViewModel : ViewModel() {


    private val repository: AuroraRepository = AuroraRepositoryImpl(
        remote = AuroraRemoteDataSource()
    )


    private val _uiState = MutableStateFlow(AuroraUiState(isLoading = true))
    val uiState: StateFlow<AuroraUiState> = _uiState.asStateFlow()


    fun loadAurora(lat: Double) {
        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true, error = null) }

            val result: Result<AuroraData> = repository.getAurora(lat)

            _uiState.update { currentState ->
                when (result) {
                    is Result.Success -> {
                        currentState.copy(
                            isLoading = false,
                            data = result.data,
                            error = null
                        )
                    }
                    is Result.Error -> {
                        currentState.copy(
                            isLoading = false,
                            data = null,
                            error = result.message
                        )
                    }
                    Result.Loading -> {
                        currentState.copy(isLoading = true)
                    }
                }
            }
        }
    }
}