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
                    Result.Error("Sijaintia ei saatu. Tarkista GPS ja luvat.")
                }
            } catch (e: SecurityException) {
                Result.Error("Sijaintilupa puuttuu. Hyväksy lupa asetuksista.")
            } catch (e: Exception) {
                Result.Error("Virhe sijaintia haettaessa: ${e.message}")
            }
        }
    }
}
