package fi.antero.aurorastars.ui.screens

import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import fi.antero.aurorastars.ui.components.ErrorMessage
import fi.antero.aurorastars.ui.components.LoadingIndicator
import fi.antero.aurorastars.viewmodel.location.LocationViewModel
import fi.antero.aurorastars.viewmodel.sky.SkyViewModel

@Composable
fun SkyScreen(navController: NavController) {
    val locationViewModel: LocationViewModel = viewModel()
    val skyViewModel: SkyViewModel = viewModel()

    val locState by locationViewModel.uiState.collectAsState()
    val skyState by skyViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (locState.location == null) {
            locationViewModel.loadLocation()
        }
    }

    LaunchedEffect(locState.location) {
        val loc = locState.location
        if (loc != null) {
            skyViewModel.loadStarMap(loc.latitude, loc.longitude)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        if (skyState.starMapUrl != null) {
            VirtualSkyPublicView(url = skyState.starMapUrl!!)
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            if (locState.isLoading) {
                LoadingIndicator()
            }
            if (locState.error != null) {
                ErrorMessage(message = locState.error!!)
                Button(onClick = { locationViewModel.loadLocation() }) {
                    Text("Yritä uudelleen")
                }
            }
        }
    }
}

@Composable
fun VirtualSkyPublicView(url: String) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                }

                webChromeClient = WebChromeClient()
                webViewClient = WebViewClient()

                loadUrl(url)
            }
        },
        update = { webView ->
            if (webView.url != url) {
                webView.loadUrl(url)
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}