package fi.antero.aurorastars.data.model.weather

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenMeteoWeatherResponse(
    val current: CurrentDto? = null,
    val daily: DailyDto? = null,
    val hourly: HourlyDto? = null
)

@Serializable
data class CurrentDto(
    @SerialName("temperature_2m")
    val temperature2m: Double? = null,

    @SerialName("weather_code")
    val weatherCode: Int? = null,

    @SerialName("cloud_cover")
    val cloudCover: Int? = null,

    @SerialName("wind_speed_10m")
    val windSpeed10m: Double? = null
)

@Serializable
data class DailyDto(
    val sunrise: List<String>? = null,
    val sunset: List<String>? = null
)

@Serializable
data class HourlyDto(
    val time: List<String>? = null,

    @SerialName("temperature_2m")
    val temperature2m: List<Double>? = null,

    @SerialName("weather_code")
    val weatherCode: List<Int>? = null,

    @SerialName("cloud_cover")
    val cloudCover: List<Int>? = null
)
