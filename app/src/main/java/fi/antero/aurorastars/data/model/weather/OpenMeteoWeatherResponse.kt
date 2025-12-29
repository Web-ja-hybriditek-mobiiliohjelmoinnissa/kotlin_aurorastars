package fi.antero.aurorastars.data.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenMeteoWeatherResponse(
    val current: CurrentDto,
    val daily: DailyDto,
    val hourly: HourlyDto? = null
)

@Serializable
data class CurrentDto(
    @SerialName("temperature_2m") val temperature2m: Double,
    @SerialName("weather_code") val weatherCode: Int,
    @SerialName("cloud_cover") val cloudCover: Int,
    @SerialName("wind_speed_10m") val windSpeed10m: Double
)

@Serializable
data class DailyDto(
    val sunrise: List<String>,
    val sunset: List<String>
)

@Serializable
data class HourlyDto(
    val time: List<String>,
    @SerialName("temperature_2m") val temperature2m: List<Double>,
    @SerialName("weather_code") val weatherCode: List<Int>
)
