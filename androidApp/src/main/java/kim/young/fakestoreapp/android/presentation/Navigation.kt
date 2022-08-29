package kim.young.fakestoreapp.android.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kim.young.fakestoreapp.shared.Parcelable
import kim.young.fakestoreapp.shared.presentation.ProductsViewModel
import org.koin.androidx.compose.inject


@Composable
fun NavHostEntry() {

    val viewModel: ProductsViewModel by inject()
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = DestinationScreen.Products.route
    ) {

        composable(DestinationScreen.Products.route) {
            ProductsScreen(viewModel = viewModel, navController = navController)
        }
        composable(
            DestinationScreen.Detail.route,
            arguments = listOf(
                navArgument("productId") {
                    type = NavType.IntType
                }
            )) {
            val productId = remember {
                it.arguments?.getInt("productId")
            }
            DetailScreen(viewModel = viewModel, navController = navController, productId?: -1)
        }
    }

}


// Navigation Destination Objects
sealed class DestinationScreen(val route: String) {
    object Products : DestinationScreen("products")
    object Detail : DestinationScreen("detail/{productId}") {
        fun createRoute(productId: Int?) = "detail/${productId}"
    }
}

data class NavParam(
    val name: String,
    val value: Parcelable
)

// Navigation Function save parcelable data as
fun navigateTo(
    navController: NavController,
    dest: DestinationScreen,
    param: NavParam
) {
    Log.e("Navigation", "fun navigateTo Called ${param.value}")
    Log.e("Navigation", "fun navigateTo Called Destination: ${dest.route}")

//    navController.currentBackStackEntry?.arguments?.putParcelable(param.name, param.value)
    navController.navigate(dest.route) {
        popUpTo(dest.route)
    }
}


