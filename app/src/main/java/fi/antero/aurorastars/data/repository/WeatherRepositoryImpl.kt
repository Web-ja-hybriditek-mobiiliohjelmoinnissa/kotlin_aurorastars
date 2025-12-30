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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs
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
                        val cloudForecast: CloudCoverForecast? = buildCloudCoverForecast(dto)

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
                                cloudCoverForecast = cloudForecast
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
            if (h.temperature2m == null || h.weatherCode == null) return null
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
            pick(6, "Aamu"),
            pick(12, "Päivä"),
            pick(18, "Ilta"),
            pick(24, "Yö")
        )
    }

    private fun buildCloudCoverForecast(dto: OpenMeteoWeatherResponse): CloudCoverForecast? {
        val hourly = dto.hourly ?: return null
        val times = hourly.time ?: return null
        val clouds = hourly.cloudCover ?: return null

        val nowCloud = dto.current?.cloudCover ?: return null
        val currentIso = dto.current?.time ?: return null

        val fmt = DateTimeFormatter.ISO_DATE_TIME
        val currentTime = runCatching { LocalDateTime.parse(currentIso, fmt) }.getOrNull() ?: return null

        fun idxClosestTo(target: LocalDateTime): Int? {
            var bestIdx: Int? = null
            var bestDiff: Long = Long.MAX_VALUE

            for (i in times.indices) {
                val t = runCatching { LocalDateTime.parse(times[i], fmt) }.getOrNull() ?: continue
                val diff = abs(java.time.Duration.between(t, target).toMinutes())
                if (diff < bestDiff) {
                    bestDiff = diff
                    bestIdx = i
                }
            }
            return bestIdx
        }

        val baseIdx = idxClosestTo(currentTime) ?: return null

        fun pick(offsetHours: Int): Int {
            val idx = (baseIdx + offsetHours).coerceIn(0, clouds.size - 1)
            return clouds[idx]
        }

        return CloudCoverForecast(
            now = nowCloud,
            h3 = pick(3),
            h6 = pick(6),
            h12 = pick(12)
        )
    }
}
