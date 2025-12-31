package fi.antero.aurorastars.ui.components.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fi.antero.aurorastars.R
import fi.antero.aurorastars.data.model.weather.WeatherData
import fi.antero.aurorastars.ui.components.InfoCard

@Composable
fun WeatherDetailsSection(data: WeatherData) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(top = 24.dp)
        ) {
            InfoCard(
                title = stringResource(R.string.sunrise),
                value = data.sunriseTime,
                modifier = Modifier.size(width = 150.dp, height = 88.dp)
            )
            InfoCard(
                title = stringResource(R.string.sunset),
                value = data.sunsetTime,
                modifier = Modifier.size(width = 150.dp, height = 88.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(top = 10.dp)
        ) {
            InfoCard(
                title = stringResource(R.string.clouds),
                value = stringResource(R.string.percent_value, data.cloudCoverPercent),
                modifier = Modifier.size(width = 150.dp, height = 88.dp)
            )
            InfoCard(
                title = stringResource(R.string.wind),
                value = stringResource(
                    R.string.wind_value,
                    String.format("%.1f", data.windSpeedMs)
                ),
                modifier = Modifier.size(width = 150.dp, height = 88.dp)
            )
        }
    }
}