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
                        val probability = calculateProbability(res.data.kpIndex, region)
                        Result.Success(
                            AuroraData(
                                kpIndex = res.data.kpIndex,
                                probabilityPercent = probability,
                                timeTag = res.data.timeTag
                            )
                        )
                    }
                    is Result.Error -> Result.Error(res.message)
                    Result.Loading -> Result.Loading
                }
            } catch (e: Exception) {
                Result.Error("AURORA_DATA_ERROR")
            }
        }
    }

    private fun calculateProbability(kp: Double, region: Region): Int {

        val (baseKp, multiplier) = when (region) {
            Region.LAPLAND_NORTH -> 0.5 to 35.0
            Region.LAPLAND_SOUTH -> 1.5 to 30.0
            Region.NORTH_OSTROBOTHNIA -> 3.0 to 25.0
            Region.CENTRAL_FINLAND -> 4.5 to 25.0
            Region.SOUTH_FINLAND -> 5.8 to 40.0
        }

        val diff = kp - baseKp

        val baseProb = 50.0 + (diff * multiplier)

        return baseProb.roundToInt().coerceIn(0, 100)
    }
}