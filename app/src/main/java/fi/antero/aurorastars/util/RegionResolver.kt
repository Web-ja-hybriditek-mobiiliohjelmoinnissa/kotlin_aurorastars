package fi.antero.aurorastars.util

import fi.antero.aurorastars.data.model.common.Region

object RegionResolver {
    fun resolve(latitude: Double): Region {
        return when {
            latitude >= 64.0 -> Region.NORTH
            latitude >= 62.0 -> Region.CENTRAL
            else -> Region.SOUTH
        }
    }
}
