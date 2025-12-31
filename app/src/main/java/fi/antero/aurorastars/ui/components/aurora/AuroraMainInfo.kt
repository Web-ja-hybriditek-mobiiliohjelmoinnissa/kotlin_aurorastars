package fi.antero.aurorastars.ui.components.aurora

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fi.antero.aurorastars.R
import fi.antero.aurorastars.data.model.aurora.AuroraData
import fi.antero.aurorastars.util.AuroraTimeUtils

@Composable
fun AuroraMainInfo(data: AuroraData) {
    val probabilityText = when {
        data.probabilityPercent < 10 -> stringResource(R.string.prob_none)
        data.probabilityPercent < 30 -> stringResource(R.string.prob_low)
        data.probabilityPercent < 60 -> stringResource(R.string.prob_medium)
        data.probabilityPercent < 85 -> stringResource(R.string.prob_high)
        else -> stringResource(R.string.prob_very_high)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Text(
            text = stringResource(R.string.aurora_title),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(
                R.string.kp_value,
                String.format("%.1f", data.kpIndex)
            ),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = stringResource(
                R.string.percent_value,
                data.probabilityPercent
            ),
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = probabilityText,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))


        Text(
            text = stringResource(
                R.string.updated_at,
                AuroraTimeUtils.noaaUtcToHelsinkiDisplay(data.timeTag)
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}