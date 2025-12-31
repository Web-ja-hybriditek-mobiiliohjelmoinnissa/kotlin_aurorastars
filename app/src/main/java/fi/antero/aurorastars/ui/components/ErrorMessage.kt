package fi.antero.aurorastars.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import fi.antero.aurorastars.R

@Composable
fun ErrorMessage(errorCode: String?) {

    val message = when (errorCode) {
        "PERMISSION_DENIED" -> stringResource(R.string.error_location_permission_denied)
        "LOCATION_NOT_FOUND" -> stringResource(R.string.error_location_not_found)
        "UNKNOWN_LOCATION_ERROR" -> stringResource(R.string.error_location_unknown)
        null -> stringResource(R.string.error_location_unknown)
        else -> stringResource(R.string.error_prefix, errorCode)
    }

    Text(
        text = message,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.error
    )
}