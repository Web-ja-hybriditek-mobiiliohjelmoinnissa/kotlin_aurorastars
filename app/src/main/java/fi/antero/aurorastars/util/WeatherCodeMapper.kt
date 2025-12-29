package fi.antero.aurorastars.util

object WeatherCodeMapper {
    fun descriptionFi(weatherCode: Int): String {
        return when (weatherCode) {
            0 -> "Selkeää"
            1 -> "Enimmäkseen selkeää"
            2 -> "Puolipilvistä"
            3 -> "Pilvistä"
            45, 48 -> "Sumuista"
            51, 53, 55 -> "Tihkusadetta"
            61, 63, 65 -> "Sadetta"
            71, 73, 75 -> "Lunta"
            80, 81, 82 -> "Kuurosadetta"
            95, 96, 99 -> "Ukkosta"
            else -> "Vaihtuvaa"
        }
    }
}
