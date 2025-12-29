package fi.antero.aurorastars.data.repository

import fi.antero.aurorastars.data.model.common.LocationInfo
import fi.antero.aurorastars.util.Result

interface LocationRepository {
    suspend fun fetchCurrentLocation(): Result<LocationInfo>
}
