package fi.antero.aurorastars.data.repository

import fi.antero.aurorastars.data.model.aurora.AuroraData
import fi.antero.aurorastars.data.model.common.Region
import fi.antero.aurorastars.data.source.aurora.AuroraRemoteDataSource
import fi.antero.aurorastars.util.RegionResolver
import fi.antero.aurorastars.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class AuroraRepositoryImpl(
    private val remote: AuroraRemoteDataSource
) : AuroraRepository {

    override suspend fun getAurora(lat: Double): Result<AuroraData> {
        return withContext(Dispatchers.IO) {
            try {
                val region = RegionResolver.resolve(lat)

                when (val res = remote.fetchLatestKp()) {
                    is Result.Success -> {
                        val entry = res.data

                        val probability = calculateProbability(entry.kpIndex, region)

                        Result.Success(
                            AuroraData(
                                kpIndex = entry.kpIndex,
                                probabilityPercent = probability,
                                timeTag = entry.timeTag
                            )
                        )
                    }

                    is Result.Error -> Result.Error(res.message)
                    Result.Loading -> Result.Loading
                }
            } catch (e: Exception) {
                Result.Error("Virhe revontulidatassa: ${e.localizedMessage}")
            }
        }
    }

    private fun calculateProbability(kp: Double, region: Region): Int {
        val baseKp = when (region) {
            Region.NORTH -> 2.0
            Region.CENTRAL -> 3.5
            Region.SOUTH -> 5.0
        }

        val diff = kp - baseKp
        val baseProb = 50.0 + (diff * 25.0)

        return baseProb.roundToInt().coerceIn(0, 100)
    }


}