package fi.antero.aurorastars.data.repository

import fi.antero.aurorastars.data.model.aurora.AuroraData
import fi.antero.aurorastars.util.Result

interface AuroraRepository {
    suspend fun getAurora(lat: Double): Result<AuroraData>
}
