package kim.young.fakestoreapp.android

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import kim.young.fakestoreapp.android.presentation.util.NavHostEntry
import kim.young.fakestoreapp.android.theme.FakeStoreAppTheme

class MainActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FakeStoreAppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    NavHostEntry()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FakeStoreAppTheme() {
        FakeStoreApplication()
    }
}
