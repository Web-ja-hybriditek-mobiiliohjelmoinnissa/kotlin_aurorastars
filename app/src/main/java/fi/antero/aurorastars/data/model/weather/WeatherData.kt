package fi.antero.aurorastars.data.model.weather

data class ForecastItem(
    val label: String,
    val temperatureC: Int,
    val weatherCode: Int,
    val isNight: Boolean
)

data class WeatherData(
    val placeName: String,
    val temperatureC: Int,
    val descriptionFi: String,
    val weatherCode: Int,
    val isNight: Boolean,
    val sunriseTime: String,
    val sunsetTime: String,
    val cloudCoverPercent: Int,
    val windSpeedMs: Double,
    val forecasts: List<ForecastItem> = emptyList(),
    val cloudCoverForecast: CloudCoverForecast? = null
)
