package fi.antero.aurorastars.viewmodel.location

import fi.antero.aurorastars.data.model.common.LocationInfo
import fi.antero.aurorastars.data.model.common.Region

data class LocationUiState(
    val isLoading: Boolean,
    val location: LocationInfo?,
    val region: Region?,
    val error: String?
)
