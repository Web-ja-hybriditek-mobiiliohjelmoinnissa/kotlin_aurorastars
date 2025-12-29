package fi.antero.aurorastars.viewmodel.info

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import fi.antero.aurorastars.util.Result

class InfoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        InfoUiState(
            Result.Success(
                "AuroraStars näyttää sään, revontulien mahdollisuuden ja tähtitaivaan."
            )
        )
    )
    val uiState = _uiState.asStateFlow()
}
