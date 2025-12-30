package fi.antero.aurorastars.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import fi.antero.aurorastars.util.rememberCurrentDayAndTime

@Composable
fun TopClock(modifier: Modifier = Modifier) {
    Text(
        text = rememberCurrentDayAndTime(),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.End,
        modifier = modifier
    )
}
