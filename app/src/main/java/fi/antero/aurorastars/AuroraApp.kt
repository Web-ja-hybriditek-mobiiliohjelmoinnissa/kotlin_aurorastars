package fi.antero.aurorastars

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import fi.antero.aurorastars.navigation.AuroraNavHost
import fi.antero.aurorastars.ui.components.AuroraBottomBar
import fi.antero.aurorastars.ui.components.LocationPermissionGate
import fi.antero.aurorastars.ui.components.rememberFineLocationPermissionState
import fi.antero.aurorastars.ui.theme.AuroraStarsTheme

@Composable
fun AuroraApp() {
    val navController = rememberNavController()

    val (hasPermission, requestPermission) = rememberFineLocationPermissionState()

    LaunchedEffect(Unit) {
        requestPermission()
    }

    AuroraStarsTheme {
        LocationPermissionGate(
            hasPermission = hasPermission,
            onRequestPermission = requestPermission
        ) {
            Scaffold(
                bottomBar = { AuroraBottomBar(navController) }
            ) { padding ->
                Box(
                    modifier = Modifier.padding(padding)
                ) {
                    AuroraNavHost(navController)
                }
            }
        }
    }
}
