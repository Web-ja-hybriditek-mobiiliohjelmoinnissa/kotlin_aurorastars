package fi.antero.aurorastars.data.model.weather

data class CloudCoverForecast(
    val now: Int,
    val h3: Int,
    val h6: Int,
    val h12: Int
)
