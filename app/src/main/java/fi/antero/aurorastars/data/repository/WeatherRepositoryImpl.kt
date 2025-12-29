package fi.antero.aurorastars.data.repository

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
            val placeName: String = geocoder.getPlaceName(lat, lon)

            val result: Result<OpenMeteoWeatherResponse> = remote.fetchWeather(lat, lon)

            when (result) {
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
                            windSpeedMs = windMs
                        )
                    )
                }

                is Result.Error -> Result.Error(result.message)
                Result.Loading -> Result.Loading
            }
        }
    }
}
