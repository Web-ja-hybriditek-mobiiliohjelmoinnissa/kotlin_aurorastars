package fi.antero.aurorastars.network

object ApiRoutes {
    const val openMeteoBaseUrl: String = "https://api.open-meteo.com"

    fun openMeteoWeather(lat: Double, lon: Double): String {
        return "$openMeteoBaseUrl/v1/forecast" +
                "?latitude=$lat" +
                "&longitude=$lon" +
                "&current=temperature_2m,weather_code,wind_speed_10m,cloud_cover" +
                "&daily=sunrise,sunset" +
                "&timezone=auto"
    }
}
