package fi.antero.aurorastars.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import fi.antero.aurorastars.R
import fi.antero.aurorastars.navigation.Routes

private data class BottomNavItem(
    val route: String,
    val labelResId: Int,
    val icon: ImageVector
)

@Composable
fun AuroraBottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem(Routes.WEATHER, R.string.nav_weather, Icons.Filled.Cloud),
        BottomNavItem(Routes.AURORA, R.string.nav_aurora, Icons.Filled.AutoAwesome),
        BottomNavItem(Routes.SKY, R.string.nav_sky, Icons.Filled.Public),
        BottomNavItem(Routes.INFO, R.string.nav_info, Icons.Filled.Info),
    )

    val backStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            val selected = currentRoute == item.route
            val labelText = stringResource(item.labelResId)

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(Routes.WEATHER) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = labelText
                    )
                },
                label = { Text(labelText) }
            )
        }
    }
}