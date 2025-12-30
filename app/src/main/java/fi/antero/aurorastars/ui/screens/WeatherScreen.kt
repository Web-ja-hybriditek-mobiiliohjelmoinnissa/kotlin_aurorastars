package fi.antero.aurorastars.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
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
import fi.antero.aurorastars.ui.components.ForecastRow
import fi.antero.aurorastars.ui.components.InfoCard
import fi.antero.aurorastars.ui.components.LoadingIndicator
import fi.antero.aurorastars.ui.components.TopClock
import fi.antero.aurorastars.util.weatherIcon
import fi.antero.aurorastars.viewmodel.location.LocationViewModel
import fi.antero.aurorastars.viewmodel.weather.WeatherViewModel

@Composable
fun WeatherScreen(navController: NavController) {
    val locationViewModel: LocationViewModel = viewModel()
    val locationState by locationViewModel.uiState.collectAsState()

    val weatherViewModel: WeatherViewModel = viewModel()
    val weatherState by weatherViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (locationState.location == null && !locationState.isLoading && locationState.error == null) {
            locationViewModel.loadLocation()
        }
    }

    LaunchedEffect(locationState.location) {
        val loc = locationState.location
        if (loc != null && !weatherState.isLoading && weatherState.data == null && weatherState.error == null) {
            weatherViewModel.loadWeather(loc.latitude, loc.longitude)
        }
    }

    val data = weatherState.data

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


        when {
            weatherState.isLoading || (data == null && weatherState.error == null) -> {
                LoadingIndicator()
            }

            weatherState.error != null -> {
                Text(
                    text = stringResource(R.string.error_prefix, weatherState.error ?: ""),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(R.string.try_again),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            data != null -> {
                Text(
                    text = data.placeName,
                    style = MaterialTheme.typography.titleLarge
                )

                Icon(
                    imageVector = weatherIcon(data.weatherCode),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 18.dp)
                        .size(110.dp)
                )

                Text(
                    text = "${data.temperatureC}°",
                    style = MaterialTheme.typography.displayLarge
                )

                Text(
                    text = data.descriptionFi,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(top = 18.dp)
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
                        value = stringResource(R.string.wind_value, String.format("%.1f", data.windSpeedMs)),
                        modifier = Modifier.size(width = 150.dp, height = 88.dp)
                    )
                }

                ForecastRow(
                    forecasts = data.forecasts,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                )
            }
        }
    }
}
