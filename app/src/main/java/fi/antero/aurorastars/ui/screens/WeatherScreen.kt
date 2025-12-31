package fi.antero.aurorastars.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import fi.antero.aurorastars.ui.components.ErrorMessage
import fi.antero.aurorastars.ui.components.LoadingIndicator
import fi.antero.aurorastars.ui.components.TopClock
import fi.antero.aurorastars.ui.components.weather.ForecastRow
import fi.antero.aurorastars.ui.components.weather.WeatherDetailsSection
import fi.antero.aurorastars.ui.components.weather.WeatherMainInfo
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
    ) {
        TopClock(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 4.dp, end = 2.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when {
                weatherState.isLoading || (data == null && weatherState.error == null) -> {
                    LoadingIndicator()
                }

                weatherState.error != null -> {
                    ErrorMessage(message = stringResource(R.string.error_prefix, weatherState.error ?: ""))
                    Button(onClick = { locationViewModel.loadLocation() }) {
                        Text(stringResource(R.string.try_again))
                    }
                }

                data != null -> {
                    WeatherMainInfo(data)

                    WeatherDetailsSection(data)

                    ForecastRow(
                        forecasts = data.forecasts,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, bottom = 16.dp)
                    )
                }
            }
        }
    }
}