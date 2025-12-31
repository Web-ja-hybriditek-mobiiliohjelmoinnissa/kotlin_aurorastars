package fi.antero.aurorastars.ui.components.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fi.antero.aurorastars.R
import fi.antero.aurorastars.data.model.weather.ForecastItem
import fi.antero.aurorastars.util.weatherIcon

@Composable
fun ForecastRow(
    forecasts: List<ForecastItem>,
    modifier: Modifier = Modifier
) {
    if (forecasts.isEmpty()) return

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        forecasts.forEach { item ->
            ForecastItemCard(item, Modifier.weight(1f))
        }
    }
}

@Composable
private fun ForecastItemCard(
    item: ForecastItem,
    modifier: Modifier = Modifier
) {
    val label = when (item.hour) {
        6 -> stringResource(R.string.time_morning)
        12 -> stringResource(R.string.time_day)
        18 -> stringResource(R.string.time_evening)
        else -> stringResource(R.string.time_night)
    }

    Card(
        modifier = modifier.height(110.dp),
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Icon(
                imageVector = weatherIcon(item.weatherCode),
                contentDescription = null,
                modifier = Modifier.size(26.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${item.temperatureC}°",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}