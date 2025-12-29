package fi.antero.aurorastars.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import fi.antero.aurorastars.navigation.Routes

private data class BottomItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

@Composable
fun AuroraBottomBar(navController: NavController) {
    val items = listOf(
        BottomItem(Routes.WEATHER, "Sää", Icons.Filled.Cloud),
        BottomItem(Routes.AURORA, "Aurora", Icons.Filled.AutoAwesome),
        BottomItem(Routes.SKY, "Taivas", Icons.Filled.Public),
        BottomItem(Routes.INFO, "Info", Icons.Filled.Info),
    )

    val backStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        items.forEach { item ->
            val selected = currentRoute == item.route

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
                    androidx.compose.material3.Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) }
            )
        }
    }
}
