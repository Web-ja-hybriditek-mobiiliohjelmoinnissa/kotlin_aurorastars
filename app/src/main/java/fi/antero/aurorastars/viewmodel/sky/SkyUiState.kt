package fi.antero.aurorastars.viewmodel.sky

import fi.antero.aurorastars.util.Result

data class SkyUiState(
    val result: Result<String> = Result.Loading
)
