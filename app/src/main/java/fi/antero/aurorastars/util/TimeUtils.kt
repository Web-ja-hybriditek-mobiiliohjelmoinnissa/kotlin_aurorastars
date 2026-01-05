package fi.antero.aurorastars.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object TimeUtils {

    fun timeFromIso(isoString: String): String {
        val index = isoString.indexOf('T')
        return if (index != -1 && isoString.length >= index + 6) {
            isoString.substring(index + 1, index + 6)
        } else {
            isoString
        }
    }

    fun getCurrentDayAndTime(): String {
        val now = LocalDateTime.now()
        val fmt = DateTimeFormatter.ofPattern("EEE d.M. HH:mm", Locale("fi", "FI"))
        return now.format(fmt)
    }
}