package kim.young.fakestoreapp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi

import kim.young.fakestoreapp.android.theme.FakeStoreAppTheme
import kim.young.fakestoreapp.android.presentation.navigation.Navigation
import kim.young.fakestoreapp.shared.presentation.products.ProductsViewModel
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val vmProducts: ProductsViewModel by inject()
//    private val vmDetail: DetailViewModel by inject()


    @OptIn(ExperimentalCoilApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FakeStoreAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar {
                        Text(text = "Fake Store App", fontSize = 24.sp ,textAlign = TextAlign.Center)
                    }
                }, scaffoldState = rememberScaffoldState()) {

                Navigation(
                        vmProducts = vmProducts
//                        vmDetail = vmDetail
                    )
                }
            }
        }
    }

}
