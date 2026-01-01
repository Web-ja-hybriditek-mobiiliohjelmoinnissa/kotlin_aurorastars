package fi.antero.aurorastars.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fi.antero.aurorastars.R
import fi.antero.aurorastars.ui.components.InfoCard
import fi.antero.aurorastars.ui.components.PageTitle

@Composable
fun InfoScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        PageTitle(title = stringResource(R.string.info_title))

        Column(modifier = Modifier.padding(16.dp)) {

            InfoSectionTitle(stringResource(R.string.info_features_title))
            BulletPoint(stringResource(R.string.info_feature_weather))
            BulletPoint(stringResource(R.string.info_feature_aurora))
            BulletPoint(stringResource(R.string.info_feature_sky))

            Spacer(modifier = Modifier.height(24.dp))

            InfoSectionTitle(stringResource(R.string.info_tech_title))
            Text(
                text = stringResource(R.string.info_tech_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(24.dp))

            InfoCard(
                title = stringResource(R.string.info_author_title),
                value = stringResource(R.string.info_author_name),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.info_credits_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            CreditItem(
                title = stringResource(R.string.credit_virtualsky_title),
                license = stringResource(R.string.credit_virtualsky_license)
            )
            CreditItem(
                title = stringResource(R.string.credit_openmeteo_title),
                license = stringResource(R.string.credit_openmeteo_license)
            )
            CreditItem(
                title = stringResource(R.string.credit_noaa_title),
                license = stringResource(R.string.credit_noaa_license)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.info_version),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun InfoSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun BulletPoint(text: String) {
    Text(
        text = "• $text",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    )
}

@Composable
private fun CreditItem(title: String, license: String) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = "• $title",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "   ${stringResource(R.string.nav_info)}: $license",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}