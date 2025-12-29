package fi.antero.aurorastars.data.repository

import fi.antero.aurorastars.data.model.weather.CloudCoverForecast
import fi.antero.aurorastars.data.model.weather.ForecastItem
import fi.antero.aurorastars.data.model.weather.OpenMeteoWeatherResponse
import fi.antero.aurorastars.data.model.weather.WeatherData
import fi.antero.aurorastars.data.source.location.GeocoderDataSource
import fi.antero.aurorastars.data.source.weather.WeatherRemoteDataSource
import fi.antero.aurorastars.util.Result
import fi.antero.aurorastars.util.TimeUtils
import fi.antero.aurorastars.util.WeatherCodeMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class WeatherRepositoryImpl(
    private val remote: WeatherRemoteDataSource,
    private val geocoder: GeocoderDataSource
) : WeatherRepository {

    override suspend fun getWeather(lat: Double, lon: Double): Result<WeatherData> {

        return withContext(Dispatchers.IO) {
            try {
                val placeName: String = try {
                    geocoder.getPlaceName(lat, lon)
                } catch (e: Exception) {
                    "Tuntematon sijainti"
                }

                val result: Result<OpenMeteoWeatherResponse> = remote.fetchWeather(lat, lon)

                when (result) {
                    is Result.Success -> {
                        val dto: OpenMeteoWeatherResponse = result.data

                        val sunriseIso: String = dto.daily?.sunrise?.firstOrNull() ?: ""
                        val sunsetIso: String = dto.daily?.sunset?.firstOrNull() ?: ""

                        val sunriseTime: String = TimeUtils.timeFromIso(sunriseIso)
                        val sunsetTime: String = TimeUtils.timeFromIso(sunsetIso)

                        val temperatureC: Int = dto.current?.temperature2m?.roundToInt() ?: 0
                        val weatherCode: Int = dto.current?.weatherCode ?: 0
                        val descriptionFi: String = WeatherCodeMapper.descriptionFi(weatherCode)

                        val cloudCover: Int = dto.current?.cloudCover ?: 0
                        val windMs: Double = (dto.current?.windSpeed10m ?: 0.0) / 3.6

                        val isNight: Boolean = false

                        val forecasts: List<ForecastItem> = buildForecasts(dto)
                        val cloudCoverForecast: CloudCoverForecast? = buildCloudCoverForecast(dto)

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
                                forecasts = forecasts,
                                cloudCoverForecast = cloudCoverForecast
                            )
                        )
                    }

                    is Result.Error -> Result.Error(result.message)
                    Result.Loading -> Result.Loading
                }
            } catch (e: Exception) {
                Result.Error("Virhe datan käsittelyssä: ${e.localizedMessage}")
            }
        }
    }

    private fun buildForecasts(dto: OpenMeteoWeatherResponse): List<ForecastItem> {
        val h = dto.hourly ?: return emptyList()

        fun pick(idx: Int, label: String): ForecastItem? {
            val temps = h.temperature2m ?: return null
            val codes = h.weatherCode ?: return null
            if (idx < 0) return null
            if (idx >= temps.size) return null
            if (idx >= codes.size) return null

            return ForecastItem(
                label = label,
                temperatureC = temps[idx].roundToInt(),
                weatherCode = codes[idx],
                isNight = false
            )
        }

        return listOfNotNull(
            pick(6, "Aamu"),
            pick(12, "Päivä"),
            pick(18, "Ilta"),
            pick(24, "Yö")
        )
    }

    private fun buildCloudCoverForecast(dto: OpenMeteoWeatherResponse): CloudCoverForecast? {
        val h = dto.hourly ?: return null
        val times = h.time ?: return null
        val clouds = h.cloudCover ?: return null
        if (times.isEmpty() || clouds.isEmpty()) return null

        val currentTime = dto.current?.time
        val baseIdx = if (currentTime != null) {
            val idx = times.indexOf(currentTime)
            if (idx >= 0) idx else 0
        } else {
            0
        }

        fun pick(idx: Int): Int {
            if (idx < 0) return 0
            if (idx >= clouds.size) return clouds.lastOrNull() ?: 0
            return clouds[idx]
        }

        return CloudCoverForecast(
            now = pick(baseIdx),
            h3 = pick(baseIdx + 3),
            h6 = pick(baseIdx + 6),
            h12 = pick(baseIdx + 12)
        )
    }
}
