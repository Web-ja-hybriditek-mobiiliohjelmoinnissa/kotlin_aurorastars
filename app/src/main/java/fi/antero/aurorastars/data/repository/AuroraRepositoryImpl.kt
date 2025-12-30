package fi.antero.aurorastars.data.repository

import fi.antero.aurorastars.data.model.aurora.AuroraData
import fi.antero.aurorastars.data.source.aurora.AuroraRemoteDataSource
import fi.antero.aurorastars.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class AuroraRepositoryImpl(
    private val remote: AuroraRemoteDataSource
) : AuroraRepository {

    override suspend fun getAurora(): Result<AuroraData> {
        return withContext(Dispatchers.IO) {
            try {
                when (val res = remote.fetchLatestKp()) {
                    is Result.Success -> {
                        val entry = res.data

                        val probability = kpToProbability(entry.kpIndex)
                        val labelFi = kpToLabelFi(entry.kpIndex)

                        Result.Success(
                            AuroraData(
                                kpIndex = entry.kpIndex,
                                probabilityPercent = probability,
                                levelLabelFi = labelFi,
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

    private fun kpToProbability(kp: Double): Int {
        return ((kp / 9.0) * 100.0).roundToInt().coerceIn(0, 100)
    }

    private fun kpToLabelFi(kp: Double): String {
        return when {
            kp < 2.0 -> "Epätodennäköinen"
            kp < 4.0 -> "Mahdollinen"
            kp < 6.0 -> "Todennäköinen"
            else -> "Erittäin todennäköinen"
        }
    }
}
