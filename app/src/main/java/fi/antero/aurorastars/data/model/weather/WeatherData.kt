package fi.antero.aurorastars.data.model.weather

data class WeatherData(
    val placeName: String,
    val temperatureC: Int,
    val descriptionFi: String,
    val weatherCode: Int,
    val isNight: Boolean,
    val sunriseTime: String,
    val sunsetTime: String,
    val cloudCoverPercent: Int,
    val windSpeedMs: Double
)
