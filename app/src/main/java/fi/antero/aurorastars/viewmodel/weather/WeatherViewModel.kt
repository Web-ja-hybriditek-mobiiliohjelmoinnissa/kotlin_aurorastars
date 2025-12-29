package fi.antero.aurorastars.viewmodel.weather

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import fi.antero.aurorastars.data.model.weather.WeatherData
import fi.antero.aurorastars.data.repository.WeatherRepository
import fi.antero.aurorastars.data.repository.WeatherRepositoryImpl
import fi.antero.aurorastars.data.source.location.GeocoderDataSource
import fi.antero.aurorastars.data.source.weather.WeatherRemoteDataSource
import fi.antero.aurorastars.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: WeatherRepository = WeatherRepositoryImpl(
        remote = WeatherRemoteDataSource(),
        geocoder = GeocoderDataSource(application.applicationContext)
    )

    private val _uiState: MutableStateFlow<WeatherUiState> = MutableStateFlow(
        WeatherUiState(
            isLoading = false,
            data = null,
            error = null
        )
    )
    val uiState: StateFlow<WeatherUiState> = _uiState

    fun loadWeather(lat: Double, lon: Double): Unit {
        viewModelScope.launch {
            _uiState.value = WeatherUiState(isLoading = true, data = null, error = null)

            val result: Result<WeatherData> = repository.getWeather(lat, lon)

            when (result) {
                is Result.Success -> {
                    _uiState.value = WeatherUiState(isLoading = false, data = result.data, error = null)
                }

                is Result.Error -> {
                    _uiState.value = WeatherUiState(isLoading = false, data = null, error = result.message)
                }

                Result.Loading -> {
                    _uiState.value = WeatherUiState(isLoading = true, data = null, error = null)
                }
            }
        }
    }
}
