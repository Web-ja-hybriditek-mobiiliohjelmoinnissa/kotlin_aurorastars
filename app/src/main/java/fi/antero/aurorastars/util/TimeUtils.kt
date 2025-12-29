package fi.antero.aurorastars.util

object TimeUtils {
    fun timeFromIso(isoString: String): String {
        val index: Int = isoString.indexOf('T')
        return if (index != -1 && isoString.length >= index + 6) {
            isoString.substring(index + 1, index + 6)
        } else {
            isoString
        }
    }
}
