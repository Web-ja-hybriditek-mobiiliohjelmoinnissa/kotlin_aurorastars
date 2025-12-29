package fi.antero.aurorastars.viewmodel.info

import fi.antero.aurorastars.util.Result

data class InfoUiState(
    val result: Result<String> = Result.Loading
)
