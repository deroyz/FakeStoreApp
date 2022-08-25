package kim.young.fakestoreapp.android.presentation.products

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import kim.young.fakestoreapp.shared.domain.ProductDomainModel
import kim.young.fakestoreapp.shared.presentation.products.ProductsViewModel

@ExperimentalCoilApi
@Composable
fun ProductsScreen(
//    onProductClick: (Long) -> Unit,
    navController: NavController,
    vm: ProductsViewModel
) {
    val state by vm.state.collectAsState()

    ProductList(products = vm.state.value.products)
}

@ExperimentalCoilApi
@Composable
fun ProductList(
    products: List<ProductDomainModel>,
//    onProductClick: (Long) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        items(products) { product ->
            ProductItem(
                product = product,
//                onClick = { product.id?.let { onProductClick(it) } }
            )
        }
    }
}
