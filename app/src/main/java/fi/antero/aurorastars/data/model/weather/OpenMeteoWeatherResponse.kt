package fi.antero.aurorastars.data.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenMeteoWeatherResponse(
    val current: CurrentWeather? = null,
    val hourly: HourlyWeather? = null,
    val daily: DailyWeather? = null
)

@Serializable
data class CurrentWeather(
    @SerialName("time") val time: String? = null,
    @SerialName("temperature_2m") val temperature2m: Double? = null,
    @SerialName("weather_code") val weatherCode: Int? = null,
    @SerialName("cloud_cover") val cloudCover: Int? = null,
    @SerialName("wind_speed_10m") val windSpeed10m: Double? = null
)

@Serializable
data class HourlyWeather(
    val time: List<String>? = null,
    @SerialName("temperature_2m") val temperature2m: List<Double>? = null,
    @SerialName("weather_code") val weatherCode: List<Int>? = null,
    @SerialName("cloud_cover") val cloudCover: List<Int>? = null
)

@Serializable
data class DailyWeather(
    val sunrise: List<String>? = null,
    val sunset: List<String>? = null
)
