package fi.antero.aurorastars.viewmodel.aurora

import fi.antero.aurorastars.data.model.aurora.AuroraData

data class AuroraUiState(
    val isLoading: Boolean,
    val data: AuroraData?,
    val error: String?
)
