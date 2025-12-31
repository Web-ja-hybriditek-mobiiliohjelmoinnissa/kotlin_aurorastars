package fi.antero.aurorastars.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object AuroraTimeUtils {

    private val helsinkiZone = ZoneId.of("Europe/Helsinki")

    private val outputFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    private val noaaFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    fun noaaUtcToHelsinkiDisplay(timeTag: String): String {
        return try {

            val instant = Instant.parse(timeTag)
            outputFormatter.withZone(helsinkiZone).format(instant)

        } catch (_: Exception) {
            try {

                val localUtc = LocalDateTime.parse(timeTag, noaaFormatter)
                val helsinkiTime = localUtc
                    .atZone(ZoneOffset.UTC)
                    .withZoneSameInstant(helsinkiZone)

                helsinkiTime.format(outputFormatter)

            } catch (_: Exception) {
                timeTag // fallback – ei kaadeta UI:ta
            }
        }
    }
}
