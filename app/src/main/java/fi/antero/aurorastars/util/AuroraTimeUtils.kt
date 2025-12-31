package fi.antero.aurorastars.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object AuroraTimeUtils {

    private val deviceZone = ZoneId.systemDefault()

    private val outputFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    private val noaaFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    fun noaaUtcToHelsinkiDisplay(timeTag: String): String {
        return try {

            val instant = Instant.parse(timeTag)
            outputFormatter.withZone(deviceZone).format(instant)

        } catch (_: Exception) {
            try {

                val localUtc = LocalDateTime.parse(timeTag, noaaFormatter)
                val localTime = localUtc
                    .atZone(ZoneOffset.UTC)
                    .withZoneSameInstant(deviceZone)

                localTime.format(outputFormatter)

            } catch (_: Exception) {

                timeTag
            }
        }
    }
}