package fi.antero.aurorastars.util

import fi.antero.aurorastars.data.model.common.Region

object RegionResolver {
    fun resolve(latitude: Double): Region {
        return when {
            latitude >= 68.5 -> Region.LAPLAND_NORTH
            latitude >= 66.5 -> Region.LAPLAND_SOUTH
            latitude >= 64.5 -> Region.NORTH_OSTROBOTHNIA
            latitude >= 62.0 -> Region.CENTRAL_FINLAND
            else -> Region.SOUTH_FINLAND
        }
    }
}