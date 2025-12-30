package fi.antero.aurorastars.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.Umbrella
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector

fun weatherIcon(weatherCode: Int): ImageVector {
    return when (weatherCode) {
        0 -> Icons.Filled.WbSunny
        1, 2 -> Icons.Filled.CloudQueue
        3 -> Icons.Filled.Cloud
        45, 48 -> Icons.Filled.Cloud
        51, 53, 55 -> Icons.Filled.WaterDrop
        61, 63, 65 -> Icons.Filled.Umbrella
        71, 73, 75 -> Icons.Filled.Cloud
        80, 81, 82 -> Icons.Filled.Umbrella
        95, 96, 99 -> Icons.Filled.Thunderstorm
        else -> Icons.Filled.Cloud
    }
}
