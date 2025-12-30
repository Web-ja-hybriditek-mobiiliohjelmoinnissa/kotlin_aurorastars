package fi.antero.aurorastars.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun rememberCurrentTimeHHmm(): String {
    var now by remember { mutableStateOf(LocalTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            now = LocalTime.now()
            delay(60_000)
        }
    }

    val fmt = DateTimeFormatter.ofPattern("HH:mm")
    return now.format(fmt)
}

@Composable
fun rememberCurrentDayAndTime(): String {
    var now by remember { mutableStateOf(LocalDateTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            now = LocalDateTime.now()
            delay(60_000)
        }
    }

    val fmt = DateTimeFormatter.ofPattern(
        "EEE d.M HH:mm",
        Locale("fi", "FI")
    )

    return now.format(fmt)
}
