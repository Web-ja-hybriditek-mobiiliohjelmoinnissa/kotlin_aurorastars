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
                        val labelFi = getLabelFi(probability)

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

    private fun calculateProbability(kp: Double, region: Region): Int {
        // Määritellään "riittävä" Kp-arvo kullekin alueelle
        val baseKp = when (region) {
            Region.NORTH -> 2.0  // Lapissa näkyy jo pienelläkin
            Region.CENTRAL -> 3.5 // Oulun korkeudella tarvitaan vähän enemmän
            Region.SOUTH -> 5.0   // Helsingissä tarvitaan kunnon myrsky
        }

        // Laskukaava: Jos Kp on sama kuin baseKp, todennäköisyys on n. 50%.
        // Jos yli, kasvaa nopeasti.
        // Esim. NORTH (base 2.0): Kp 2 -> 50%, Kp 4 -> 100%
        // Esim. SOUTH (base 5.0): Kp 3 -> 0%, Kp 5 -> 50%, Kp 7 -> 100%

        val diff = kp - baseKp
        val baseProb = 50.0 + (diff * 25.0)

        return baseProb.roundToInt().coerceIn(0, 100)
    }

    private fun getLabelFi(prob: Int): String {
        return when {
            prob < 10 -> "Ei odotettavissa"
            prob < 30 -> "Epätodennäköinen"
            prob < 60 -> "Mahdollinen"
            prob < 85 -> "Todennäköinen"
            else -> "Erittäin todennäköinen"
        }
    }
}