package fi.antero.aurorastars.data.repository

import fi.antero.aurorastars.data.model.weather.ForecastItem
import fi.antero.aurorastars.data.model.weather.OpenMeteoWeatherResponse
import fi.antero.aurorastars.data.model.weather.WeatherData
import fi.antero.aurorastars.data.source.location.GeocoderDataSource
import fi.antero.aurorastars.data.source.weather.WeatherRemoteDataSource
import fi.antero.aurorastars.util.Result
import fi.antero.aurorastars.util.TimeUtils
import fi.antero.aurorastars.util.WeatherCodeMapper
import kotlin.math.roundToInt

class WeatherRepositoryImpl(
    private val remote: WeatherRemoteDataSource,
    private val geocoder: GeocoderDataSource
) : WeatherRepository {

    override suspend fun getWeather(lat: Double, lon: Double): Result<WeatherData> {
        val placeName: String = geocoder.getPlaceName(lat, lon)
        val result: Result<OpenMeteoWeatherResponse> = remote.fetchWeather(lat, lon)

        return when (result) {
            is Result.Success -> {
                val dto: OpenMeteoWeatherResponse = result.data

                val sunriseIso: String = dto.daily.sunrise.firstOrNull() ?: ""
                val sunsetIso: String = dto.daily.sunset.firstOrNull() ?: ""

                val sunriseTime: String = TimeUtils.timeFromIso(sunriseIso)
                val sunsetTime: String = TimeUtils.timeFromIso(sunsetIso)

                val temperatureC: Int = dto.current.temperature2m.roundToInt()
                val weatherCode: Int = dto.current.weatherCode
                val descriptionFi: String = WeatherCodeMapper.descriptionFi(weatherCode)

                val cloudCover: Int = dto.current.cloudCover
                val windMs: Double = dto.current.windSpeed10m / 3.6

                val isNight: Boolean = false

                val forecasts: List<ForecastItem> = buildForecasts(dto)

                Result.Success(
                    WeatherData(
                        placeName = placeName,
                        temperatureC = temperatureC,
                        descriptionFi = descriptionFi,
                        weatherCode = weatherCode,
                        isNight = isNight,
                        sunriseTime = sunriseTime,
                        sunsetTime = sunsetTime,
                        cloudCoverPercent = cloudCover,
                        windSpeedMs = windMs,
                        forecasts = forecasts
                    )
                )
            }

            is Result.Error -> Result.Error(result.message)
            Result.Loading -> Result.Loading
        }
    }

    private fun buildForecasts(dto: OpenMeteoWeatherResponse): List<ForecastItem> {
        val h = dto.hourly ?: return emptyList()

        fun pick(idx: Int, label: String): ForecastItem? {
            if (idx < 0) return null
            if (idx >= h.temperature2m.size) return null
            if (idx >= h.weatherCode.size) return null

            return ForecastItem(
                label = label,
                temperatureC = h.temperature2m[idx].roundToInt(),
                weatherCode = h.weatherCode[idx],
                isNight = false
            )
        }

        return listOfNotNull(
            pick(3, "3h"),
            pick(12, "12h"),
            pick(24, "24h")
        )
    }
}
