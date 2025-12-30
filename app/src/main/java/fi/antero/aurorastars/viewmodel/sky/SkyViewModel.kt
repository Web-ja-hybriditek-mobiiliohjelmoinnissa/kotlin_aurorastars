package fi.antero.aurorastars.viewmodel.sky

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class SkyViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SkyUiState())
    val uiState = _uiState.asStateFlow()

    fun loadStarMap(lat: Double, lon: Double) {
        val latStr = String.format(Locale.US, "%.4f", lat)
        val lonStr = String.format(Locale.US, "%.4f", lon)

        val url = "https://virtualsky.lco.global/embed/index.html" +
                "?longitude=$lonStr" +
                "&latitude=$latStr" +
                "&projection=stereo" +
                "&constellations=true" +
                "&constellationlabels=true" +
                "&showstarlabels=true" +
                "&live=true" +
                "&az=180" +
                "&scalestars=2" +
                "&mag=5"

        _uiState.value = SkyUiState(starMapUrl = url)
    }
}