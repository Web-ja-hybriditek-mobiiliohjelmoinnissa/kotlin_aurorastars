package fi.antero.aurorastars.data.repository

import fi.antero.aurorastars.data.model.weather.WeatherData
import fi.antero.aurorastars.util.Result

interface WeatherRepository {
    suspend fun getWeather(lat: Double, lon: Double): Result<WeatherData>
}
