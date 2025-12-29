package fi.antero.aurorastars.viewmodel.aurora

import fi.antero.aurorastars.util.Result

data class AuroraUiState(
    val result: Result<String> = Result.Loading
)
