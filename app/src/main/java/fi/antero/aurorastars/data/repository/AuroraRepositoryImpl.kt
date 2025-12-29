package fi.antero.aurorastars.data.repository

import fi.antero.aurorastars.data.model.aurora.AuroraData
import fi.antero.aurorastars.data.model.aurora.NoaaPlanetaryKIndexEntry
import fi.antero.aurorastars.data.source.aurora.AuroraRemoteDataSource
import fi.antero.aurorastars.util.Result
import kotlin.math.abs
import kotlin.math.roundToInt

class AuroraRepositoryImpl(
    private val remote: AuroraRemoteDataSource
) : AuroraRepository {

    override suspend fun getAurora(lat: Double): Result<AuroraData> {
        val kpResult: Result<NoaaPlanetaryKIndexEntry> = remote.fetchLatestKp()

        return when (kpResult) {
            is Result.Success -> {
                val entry: NoaaPlanetaryKIndexEntry = kpResult.data
                val kp: Double = entry.kpIndex

                val threshold: Double = kpThresholdForLatitude(lat)
                val raw: Double = (kp - threshold) * 25.0
                val prob: Int = raw.coerceIn(0.0, 100.0).roundToInt()

                val level: String = when {
                    prob >= 70 -> "Hyvä"
                    prob >= 40 -> "Kohtalainen"
                    prob >= 15 -> "Heikko"
                    else -> "Epätodennäköinen"
                }

                Result.Success(
                    AuroraData(
                        kpIndex = kp,
                        probabilityPercent = prob,
                        levelText = level,
                        updatedTime = entry.timeTag
                    )
                )
            }

            is Result.Error -> Result.Error(kpResult.message)
            Result.Loading -> Result.Loading
        }
    }

    private fun kpThresholdForLatitude(lat: Double): Double {
        val a: Double = abs(lat)
        return when {
            a >= 69.0 -> 2.0
            a >= 66.0 -> 3.0
            a >= 63.0 -> 4.0
            a >= 60.0 -> 5.0
            a >= 57.0 -> 6.0
            else -> 7.0
        }
    }
}
