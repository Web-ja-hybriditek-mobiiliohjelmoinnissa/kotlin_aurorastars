package fi.antero.aurorastars.util

import fi.antero.aurorastars.R

object WeatherCodeMapper {

    fun getDescriptionResId(weatherCode: Int): Int {
        return when (weatherCode) {
            0 -> R.string.weather_clear
            1 -> R.string.weather_mostly_clear
            2 -> R.string.weather_partly_cloudy
            3 -> R.string.weather_overcast
            45, 48 -> R.string.weather_fog
            51, 53, 55 -> R.string.weather_drizzle
            61, 63, 65 -> R.string.weather_rain
            71, 73, 75 -> R.string.weather_snow
            80, 81, 82 -> R.string.weather_showers
            95, 96, 99 -> R.string.weather_thunderstorm
            else -> R.string.weather_unknown
        }
    }
}