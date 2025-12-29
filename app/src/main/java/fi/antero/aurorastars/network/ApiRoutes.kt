package fi.antero.aurorastars.network

object ApiRoutes {

    fun openMeteoWeather(lat: Double, lon: Double): String {
        return "https://api.open-meteo.com/v1/forecast" +
                "?latitude=$lat" +
                "&longitude=$lon" +
                "&current=temperature_2m,weather_code,cloud_cover,wind_speed_10m" +
                "&daily=sunrise,sunset" +
                "&hourly=temperature_2m,weather_code" +
                "&timezone=auto"
    }
    fun noaaPlanetaryKIndex(): String {
        return "https://services.swpc.noaa.gov/products/noaa-planetary-k-index.json"
    }
}
