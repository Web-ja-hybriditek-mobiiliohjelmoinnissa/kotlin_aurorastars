package fi.antero.aurorastars.data.source.weather

import fi.antero.aurorastars.data.model.weather.OpenMeteoWeatherResponse
import fi.antero.aurorastars.network.ApiRoutes
import fi.antero.aurorastars.network.NetworkClient
import fi.antero.aurorastars.network.safeApiCall
import fi.antero.aurorastars.util.Result
import io.ktor.client.request.get

class WeatherRemoteDataSource {

    suspend fun fetchWeather(lat: Double, lon: Double): Result<OpenMeteoWeatherResponse> {
        val url: String = ApiRoutes.openMeteoWeather(lat, lon)

        return safeApiCall {
            NetworkClient.httpClient.get(url)
        }
    }
}
