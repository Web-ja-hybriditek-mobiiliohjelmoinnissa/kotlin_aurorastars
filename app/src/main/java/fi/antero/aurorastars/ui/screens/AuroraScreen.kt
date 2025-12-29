package fi.antero.aurorastars.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import fi.antero.aurorastars.navigation.Routes
import fi.antero.aurorastars.ui.components.*
import fi.antero.aurorastars.util.Result
import fi.antero.aurorastars.viewmodel.aurora.AuroraViewModel

@Composable
fun AuroraScreen(navController: NavController) {
    val vm: AuroraViewModel = viewModel()
    val state = vm.uiState.collectAsState().value

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        PageTitle("Revontulet")

        when (val r = state.result) {
            is Result.Loading -> LoadingIndicator()
            is Result.Error -> ErrorMessage(r.message)
            is Result.Success -> Text(r.data)
        }

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = { navController.navigate(Routes.SKY) }
        ) {
            Text("Sky →")
        }
    }
}
