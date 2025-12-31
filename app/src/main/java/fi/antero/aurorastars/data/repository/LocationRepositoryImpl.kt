package fi.antero.aurorastars.data.repository

import fi.antero.aurorastars.data.model.common.LocationInfo
import fi.antero.aurorastars.data.source.location.LocationDataSource
import fi.antero.aurorastars.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationRepositoryImpl(
    private val dataSource: LocationDataSource
) : LocationRepository {

    override suspend fun fetchCurrentLocation(): Result<LocationInfo> {
        return withContext(Dispatchers.IO) {
            try {
                val location: LocationInfo? = dataSource.getCurrentLocation()
                if (location != null) {
                    Result.Success(location)
                } else {

                    Result.Error("LOCATION_NOT_FOUND")
                }
            } catch (e: SecurityException) {
                Result.Error("PERMISSION_DENIED")
            } catch (e: Exception) {
                Result.Error(e.message ?: "UNKNOWN_LOCATION_ERROR")
            }
        }
    }
}