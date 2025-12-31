package fi.antero.aurorastars.ui.components.weather

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fi.antero.aurorastars.data.model.weather.WeatherData
import fi.antero.aurorastars.util.WeatherCodeMapper
import fi.antero.aurorastars.util.weatherIcon

@Composable
fun WeatherMainInfo(data: WeatherData) {

    val description = stringResource(WeatherCodeMapper.getDescriptionResId(data.weatherCode))

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
            text = description,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}