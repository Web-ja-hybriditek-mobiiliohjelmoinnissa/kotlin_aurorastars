package fi.antero.aurorastars.ui.components

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding

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
        Text("Tarvitsemme sijainnin, jotta voimme näyttää sään ja taivaan tämänhetkisessä paikassa.")
        Button(onClick = onRequestPermission) {
            Text("Salli sijainti")
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
