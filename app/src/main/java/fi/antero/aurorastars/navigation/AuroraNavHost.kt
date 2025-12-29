package fi.antero.aurorastars.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fi.antero.aurorastars.ui.screens.AuroraScreen
import fi.antero.aurorastars.ui.screens.InfoScreen
import fi.antero.aurorastars.ui.screens.SkyScreen
import fi.antero.aurorastars.ui.screens.WeatherScreen

@Composable
fun AuroraNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.WEATHER
    ) {
        composable(Routes.WEATHER) { WeatherScreen(navController) }
        composable(Routes.AURORA) { AuroraScreen(navController) }
        composable(Routes.SKY) { SkyScreen(navController) }
        composable(Routes.INFO) { InfoScreen(navController) }
    }
}
