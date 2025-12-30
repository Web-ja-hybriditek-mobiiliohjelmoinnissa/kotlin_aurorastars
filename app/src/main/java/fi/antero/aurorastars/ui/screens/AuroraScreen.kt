package fi.antero.aurorastars.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import fi.antero.aurorastars.R
import fi.antero.aurorastars.ui.components.LoadingIndicator
import fi.antero.aurorastars.ui.components.SmallValueCard
import fi.antero.aurorastars.ui.components.TopClock
import fi.antero.aurorastars.util.AuroraTimeUtils
import fi.antero.aurorastars.viewmodel.aurora.AuroraViewModel
import fi.antero.aurorastars.viewmodel.location.LocationViewModel
import fi.antero.aurorastars.viewmodel.weather.WeatherViewModel

@Composable
fun AuroraScreen(navController: NavController) {
    val locationViewModel: LocationViewModel = viewModel()
    val locationState by locationViewModel.uiState.collectAsState()

    val auroraViewModel: AuroraViewModel = viewModel()
    val auroraState by auroraViewModel.uiState.collectAsState()

    val weatherViewModel: WeatherViewModel = viewModel()
    val weatherState by weatherViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (locationState.location == null && !locationState.isLoading && locationState.error == null) {
            locationViewModel.loadLocation()
        }
    }

    LaunchedEffect(locationState.location) {
        val loc = locationState.location ?: return@LaunchedEffect

        if (!auroraState.isLoading && auroraState.data == null && auroraState.error == null) {
            auroraViewModel.loadAurora()
        }

        if (!weatherState.isLoading && weatherState.data == null && weatherState.error == null) {
            weatherViewModel.loadWeather(loc.latitude, loc.longitude)
        }
    }

    Box(modifier = Modifier.fillMaxWidth().padding(18.dp)) {
        TopClock(modifier = Modifier.align(Alignment.TopEnd))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        val a = auroraState.data

        when {
            auroraState.isLoading || (a == null && auroraState.error == null) -> {
                LoadingIndicator()
            }

            auroraState.error != null -> {
                Text(
                    text = stringResource(R.string.error_prefix, auroraState.error ?: ""),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            a != null -> {
                Text(
                    text = stringResource(R.string.aurora_title),
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(
                        R.string.kp_value,
                        String.format("%.1f", a.kpIndex)
                    ),
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = stringResource(
                        R.string.percent_value_string,
                        a.probabilityPercent
                    ),
                    style = MaterialTheme.typography.displayLarge
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = a.levelLabelFi,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = stringResource(
                        R.string.updated_at,
                        AuroraTimeUtils.noaaUtcToHelsinkiDisplay(a.timeTag)
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                val clouds = weatherState.data?.cloudCoverForecast
                if (clouds != null) {
                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stringResource(R.string.cloud_forecast),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        SmallValueCard(stringResource(R.string.now), "${clouds.now}%")
                        SmallValueCard(stringResource(R.string.h3), "${clouds.h3}%")
                        SmallValueCard(stringResource(R.string.h6), "${clouds.h6}%")
                        SmallValueCard(stringResource(R.string.h12), "${clouds.h12}%")
                    }
                }
            }
        }
    }
}
