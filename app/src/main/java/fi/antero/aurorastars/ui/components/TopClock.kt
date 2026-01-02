package fi.antero.aurorastars.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import fi.antero.aurorastars.util.TimeTicker
import fi.antero.aurorastars.util.TimeUtils

@Composable
fun TopClock(modifier: Modifier = Modifier) {
    var currentTimeText by remember { mutableStateOf(TimeUtils.getCurrentDayAndTime()) }

    LaunchedEffect(Unit) {
        TimeTicker.minuteFlow.collect {
            currentTimeText = TimeUtils.getCurrentDayAndTime()
        }
    }

    Text(
        text = currentTimeText,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.End,
        modifier = modifier
    )
}