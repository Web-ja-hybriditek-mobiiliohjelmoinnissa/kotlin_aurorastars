package fi.antero.aurorastars.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import fi.antero.aurorastars.data.model.weather.ForecastItem
import fi.antero.aurorastars.ui.components.LoadingIndicator
import fi.antero.aurorastars.viewmodel.location.LocationViewModel
import fi.antero.aurorastars.viewmodel.weather.WeatherViewModel
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val data = weatherState.data

        when {
            weatherState.isLoading || (data == null && weatherState.error == null) -> {
                Spacer(modifier = Modifier.height(40.dp))
                LoadingIndicator()
            }

            weatherState.error != null -> {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Virhe: ${weatherState.error}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Yritä hetken päästä uudelleen.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            data != null -> {
                val todayTime: String = todayTimeText()

                Text(
                    text = data.placeName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = todayTime,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(26.dp))

                val icon: ImageVector = weatherIcon(data.weatherCode)
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "${data.temperatureC}°",
                    fontSize = 74.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = data.descriptionFi,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    InfoCard(
                        title = "Auringonnousu",
                        value = data.sunriseTime
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    InfoCard(
                        title = "Auringonlasku",
                        value = data.sunsetTime
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    InfoCard(
                        title = "Pilvet",
                        value = "${data.cloudCoverPercent}%"
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    InfoCard(
                        title = "Tuuli",
                        value = "${String.format("%.1f", data.windSpeedMs)} m/s"
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                ForecastRow(
                    forecasts = data.forecasts,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun todayTimeText(): String {
    var now by remember { mutableStateOf(LocalTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            now = LocalTime.now()
            delay(60_000)
        }
    }

    val fmt = DateTimeFormatter.ofPattern("HH:mm")
    return "Tänään ${now.format(fmt)}"
}

@Composable
private fun ForecastRow(
    forecasts: List<ForecastItem>,
    modifier: Modifier = Modifier
) {
    if (forecasts.isEmpty()) return

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        forecasts.forEach { item ->
            ForecastMiniCard(
                label = item.label,
                temperatureC = item.temperatureC,
                weatherCode = item.weatherCode,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ForecastMiniCard(
    label: String,
    temperatureC: Int,
    weatherCode: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Icon(
                imageVector = weatherIcon(weatherCode),
                contentDescription = null,
                modifier = Modifier.size(26.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${temperatureC}°",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun InfoCard(
    title: String,
    value: String
) {
    Card(
        modifier = Modifier.width(150.dp),
        colors = CardDefaults.cardColors()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private fun weatherIcon(weatherCode: Int): ImageVector {
    return when (weatherCode) {
        0 -> Icons.Filled.WbSunny
        1, 2 -> Icons.Filled.CloudQueue
        3 -> Icons.Filled.Cloud
        45, 48 -> Icons.Filled.Cloud
        51, 53, 55 -> Icons.Filled.WaterDrop
        61, 63, 65 -> Icons.Filled.Umbrella
        71, 73, 75 -> Icons.Filled.Cloud
        80, 81, 82 -> Icons.Filled.Umbrella
        95, 96, 99 -> Icons.Filled.Thunderstorm
        else -> Icons.Filled.Cloud
    }
}
