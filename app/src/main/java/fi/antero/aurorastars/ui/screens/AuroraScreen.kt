package fi.antero.aurorastars.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import fi.antero.aurorastars.ui.components.LoadingIndicator
import fi.antero.aurorastars.util.AuroraTimeUtils
import fi.antero.aurorastars.viewmodel.aurora.AuroraViewModel
import fi.antero.aurorastars.viewmodel.location.LocationViewModel
import fi.antero.aurorastars.viewmodel.weather.WeatherViewModel

@Composable
fun AuroraScreen() {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val a = auroraState.data

        when {
            auroraState.isLoading || (a == null && auroraState.error == null) -> {
                Spacer(modifier = Modifier.height(40.dp))
                LoadingIndicator()
            }

            auroraState.error != null -> {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Virhe: ${auroraState.error}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            a != null -> {
                Text(
                    text = "Revontulet",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Kp ${String.format("%.1f", a.kpIndex)}",
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "${a.probabilityPercent} %",
                    fontSize = 54.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = a.levelLabelFi,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Päivitetty: ${AuroraTimeUtils.noaaUtcToHelsinkiDisplay(a.timeTag)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                val clouds = weatherState.data?.cloudCoverForecast
                if (clouds != null) {
                    Spacer(modifier = Modifier.height(22.dp))

                    Text(
                        text = "Pilvisyysennuste",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        SmallValueCard("Nyt", "${clouds.now}%")
                        Spacer(modifier = Modifier.width(10.dp))
                        SmallValueCard("3h", "${clouds.h3}%")
                        Spacer(modifier = Modifier.width(10.dp))
                        SmallValueCard("6h", "${clouds.h6}%")
                        Spacer(modifier = Modifier.width(10.dp))
                        SmallValueCard("12h", "${clouds.h12}%")
                    }
                }
            }
        }
    }
}
