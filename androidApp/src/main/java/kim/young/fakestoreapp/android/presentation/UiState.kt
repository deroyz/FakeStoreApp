package kim.young.fakestoreapp.android.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kim.young.fakestoreapp.android.util.ConnectionState
import kim.young.fakestoreapp.android.util.currentConnectivityState
import kim.young.fakestoreapp.android.util.observeConnectivityAsFlow
import kim.young.fakestoreapp.shared.presentation.ProductsState

@Composable
fun Empty(
    msg: String,
    onCheckAgain: () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = msg,
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.size(10.dp))
            OutlinedButton(
                onClick = onCheckAgain
            ) {
                Text(
                    text = "Check Again",
                    style = MaterialTheme.typography.h6
                )
            }
        }
    }
}


@Composable
fun Error(
    state: ProductsState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = state.error.errorMessage,
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h5
        )

    }
}

@Composable
fun Loading() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        CircularProgressIndicator()

    }
}

@Composable
fun ConnectivityState(): State<ConnectionState> {
    val context = LocalContext.current
    return produceState(context.currentConnectivityState) {
        context.observeConnectivityAsFlow().collect {
            value = it
        }
    }
}

@Composable
fun Network() {
    val connection by ConnectivityState()
    Column(Modifier.fillMaxSize(), Arrangement.Center, Alignment.CenterHorizontally) {
        if(connection == ConnectionState.Available){
        Text(text = "Back To Online", fontSize = 25.sp, fontWeight = FontWeight.Bold)
        }else{
            Text(text = "Back To Offline", fontSize = 25.sp, fontWeight = FontWeight.Bold)
        }
    }
}

