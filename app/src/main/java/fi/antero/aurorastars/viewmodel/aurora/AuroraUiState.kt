package fi.antero.aurorastars.viewmodel.aurora

import fi.antero.aurorastars.data.model.aurora.AuroraData

data class AuroraUiState(
    val isLoading: Boolean = false,
    val data: AuroraData? = null,
    val error: String? = null
)