package fi.antero.aurorastars.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import fi.antero.aurorastars.R

@Composable
fun ErrorMessage(errorCode: String?) {
    val message = when {

        errorCode == "NETWORK_NO_CONNECTION" -> stringResource(R.string.error_no_network)
        errorCode == "NETWORK_TIMEOUT" -> stringResource(R.string.error_timeout)
        errorCode == "API_REDIRECT" -> stringResource(R.string.error_api_redirect)

        errorCode?.startsWith("API_CLIENT_ERROR_") == true -> {
            val status = errorCode.removePrefix("API_CLIENT_ERROR_").toIntOrNull() ?: 0
            stringResource(R.string.error_api_client, status)
        }
        errorCode?.startsWith("API_SERVER_ERROR_") == true -> {
            val status = errorCode.removePrefix("API_SERVER_ERROR_").toIntOrNull() ?: 0
            stringResource(R.string.error_api_server, status)
        }

        errorCode == "PERMISSION_DENIED" -> stringResource(R.string.error_location_permission_denied)
        errorCode == "LOCATION_NOT_FOUND" -> stringResource(R.string.error_location_not_found)
        errorCode == "UNKNOWN_LOCATION_ERROR" -> stringResource(R.string.error_location_unknown)

        errorCode == null -> stringResource(R.string.error_location_unknown)

        else -> stringResource(R.string.error_prefix, errorCode ?: "")
    }

    Text(
        text = message,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.error
    )
}