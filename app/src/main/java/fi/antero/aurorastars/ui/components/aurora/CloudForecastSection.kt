package fi.antero.aurorastars.ui.components.aurora

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fi.antero.aurorastars.R
import fi.antero.aurorastars.data.model.weather.CloudCoverForecast

@Composable
fun CloudForecastSection(clouds: CloudCoverForecast) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.cloud_forecast),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally)
        ) {
            AuroraValueCard(stringResource(R.string.now), "${clouds.now}%")
            AuroraValueCard(stringResource(R.string.h3), "${clouds.h3}%")
            AuroraValueCard(stringResource(R.string.h6), "${clouds.h6}%")
            AuroraValueCard(stringResource(R.string.h12), "${clouds.h12}%")
        }
    }
}