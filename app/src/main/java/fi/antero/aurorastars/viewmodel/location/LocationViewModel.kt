package fi.antero.aurorastars.viewmodel.location

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fi.antero.aurorastars.data.model.common.LocationInfo
import fi.antero.aurorastars.data.model.common.Region
import fi.antero.aurorastars.data.repository.LocationRepository
import fi.antero.aurorastars.data.repository.LocationRepositoryImpl
import fi.antero.aurorastars.data.source.location.LocationDataSource
import fi.antero.aurorastars.util.RegionResolver
import fi.antero.aurorastars.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LocationRepository = LocationRepositoryImpl(
        dataSource = LocationDataSource(application.applicationContext)
    )

    private val _uiState: MutableStateFlow<LocationUiState> = MutableStateFlow(
        LocationUiState(
            isLoading = false,
            location = null,
            region = null,
            error = null
        )
    )
    val uiState: StateFlow<LocationUiState> = _uiState

    fun loadLocation(): Unit {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result: Result<LocationInfo> = repository.fetchCurrentLocation()

            when (result) {
                is Result.Success -> {
                    val loc: LocationInfo = result.data
                    val region: Region = RegionResolver.resolve(loc.latitude)

                    _uiState.value = LocationUiState(
                        isLoading = false,
                        location = loc,
                        region = region,
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
