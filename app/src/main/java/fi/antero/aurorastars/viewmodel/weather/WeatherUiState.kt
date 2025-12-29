package fi.antero.aurorastars.viewmodel.weather

import fi.antero.aurorastars.data.model.weather.WeatherData

data class WeatherUiState(
    val isLoading: Boolean,
    val data: WeatherData?,
    val error: String?
)
