package fi.antero.aurorastars.data.source.aurora

import fi.antero.aurorastars.data.model.aurora.NoaaPlanetaryKIndexEntry
import fi.antero.aurorastars.network.ApiRoutes
import fi.antero.aurorastars.network.NetworkClient
import fi.antero.aurorastars.network.safeApiCall
import fi.antero.aurorastars.util.Result
import io.ktor.client.call.body
import io.ktor.client.request.get

class AuroraRemoteDataSource {

    suspend fun fetchLatestKp(): Result<NoaaPlanetaryKIndexEntry> {
        val url: String = ApiRoutes.noaaPlanetaryKIndex()

        val responseResult: Result<List<List<String>>> = safeApiCall {
            NetworkClient.httpClient
                .get(url)
                .body()
        }

        return when (responseResult) {
            is Result.Success -> {
                val raw: List<List<String>> = responseResult.data

                val lastRow: List<String> =
                    raw.lastOrNull { it.size >= 2 && it[0] != "time_tag" }
                        ?: return Result.Error("Kp-data puuttuu")

                val timeTag: String = lastRow[0]
                val kpIndex: Double = lastRow[1].toDoubleOrNull()
                    ?: return Result.Error("Virheellinen kp-arvo")

                Result.Success(
                    NoaaPlanetaryKIndexEntry(
                        timeTag = timeTag,
                        kpIndex = kpIndex
                    )
                )
            }

            is Result.Error -> Result.Error(responseResult.message)
            Result.Loading -> Result.Loading
        }
    }
}
