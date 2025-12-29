package fi.antero.aurorastars.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import fi.antero.aurorastars.ui.components.PageTitle
import fi.antero.aurorastars.viewmodel.info.InfoViewModel

@Composable
fun InfoScreen(navController: NavController) {
    val vm: InfoViewModel = viewModel()
    val state = vm.uiState.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        PageTitle("Info")
        Text(state.result.let { (it as fi.antero.aurorastars.util.Result.Success).data })
    }
}
