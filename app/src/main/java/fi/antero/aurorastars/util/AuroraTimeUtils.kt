package fi.antero.aurorastars.util

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object AuroraTimeUtils {

    private val helsinkiZone: ZoneId = ZoneId.of("Europe/Helsinki")
    private val outFmt: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    fun noaaUtcToHelsinkiDisplay(timeTag: String): String {
        return try {
            val instant: Instant = Instant.parse(timeTag)
            val zdt: ZonedDateTime = instant.atZone(helsinkiZone)
            zdt.format(outFmt)
        } catch (e: Exception) {
            timeTag
        }
    }
}
