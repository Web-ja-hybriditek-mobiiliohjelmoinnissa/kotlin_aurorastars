package fi.antero.aurorastars.ui.components

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fi.antero.aurorastars.R

@Composable
fun LocationPermissionGate(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit,
    content: @Composable () -> Unit
) {
    if (hasPermission) {
        content()
        return
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(stringResource(R.string.location_gate_description))

        Button(onClick = onRequestPermission) {

            Text(stringResource(R.string.location_gate_button))
        }
    }
}

@Composable
fun rememberFineLocationPermissionState(): Pair<Boolean, () -> Unit> {
    val grantedState = remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        grantedState.value = granted
    }

    val request = {
        launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    return grantedState.value to request
}