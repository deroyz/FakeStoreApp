package kim.young.fakestoreapp.android.presentation.util

import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kim.young.fakestoreapp.android.presentation.DetailScreen
import kim.young.fakestoreapp.android.presentation.ProductsScreen
import kim.young.fakestoreapp.shared.Parcelable
import kim.young.fakestoreapp.shared.domain.ProductDomainModel


@Composable
fun NavHostEntry() {

    val navController = rememberNavController()

   NavHost(
        navController = navController,
        startDestination = DestinationScreen.Products.route
    ) {

        composable(DestinationScreen.Products.route) {
            ProductsScreen(navController = navController)
        }
        composable(DestinationScreen.Detail.route) {
            val productData =
                navController.previousBackStackEntry?.arguments?.getParcelable<ProductDomainModel>("product")
            Log.e("DestinationDetail Composable", "$productData")
            productData?.let {
                DetailScreen(navController = (navController), productData)
            }
        }

    }
}

sealed class DestinationScreen(val route: String) {
    object Products : DestinationScreen("products")
    object Detail : DestinationScreen("detail")
}

data class NavParam(
    val name: String,
    val value: Parcelable
)

fun navigateTo(
    navController: NavController,
    dest: DestinationScreen,
    param: NavParam
) {
    Log.e("Navigation", "fun navigateTo Called ${param.value}")
    Log.e("Navigation", "fun navigateTo Called Destination: ${dest.route}")
    val bundle = Bundle().apply{
        putParcelable(param.name, param.value)
    }
    Log.e("Navigation", "fun navigateTo Called Bundle: ${bundle.get(param.name)}")
    navController.currentBackStackEntry?.arguments?.putParcelable(param.name, param.value)
    navController.navigate(dest.route){
        popUpTo(dest.route)
        launchSingleTop = true
    }
}


