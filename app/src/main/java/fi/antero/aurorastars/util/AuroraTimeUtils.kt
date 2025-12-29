package fi.antero.aurorastars.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object AuroraTimeUtils {

    private val noaaFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    private val displayFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    fun noaaUtcToHelsinkiDisplay(timeTag: String): String {
        return try {
            val ldt: LocalDateTime = LocalDateTime.parse(timeTag, noaaFormatter)
            val utc: ZonedDateTime = ldt.atZone(ZoneOffset.UTC)
            val helsinki: ZonedDateTime = utc.withZoneSameInstant(ZoneId.of("Europe/Helsinki"))
            helsinki.format(displayFormatter)
        } catch (e: Exception) {
            timeTag
        }
    }
}
