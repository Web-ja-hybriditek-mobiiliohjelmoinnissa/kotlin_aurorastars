package fi.antero.aurorastars.viewmodel.aurora

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fi.antero.aurorastars.data.repository.AuroraRepository
import fi.antero.aurorastars.data.repository.AuroraRepositoryImpl
import fi.antero.aurorastars.data.source.aurora.AuroraRemoteDataSource
import fi.antero.aurorastars.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuroraViewModel(
    private val repo: AuroraRepository = AuroraRepositoryImpl(AuroraRemoteDataSource())
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuroraUiState())
    val uiState: StateFlow<AuroraUiState> = _uiState

    fun loadAurora() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val res = repo.getAurora()) {
                is Result.Success -> _uiState.value = AuroraUiState(isLoading = false, data = res.data, error = null)
                is Result.Error -> _uiState.value = AuroraUiState(isLoading = false, data = null, error = res.message)
                Result.Loading -> _uiState.value = _uiState.value.copy(isLoading = true)
            }
        }
    }
}
