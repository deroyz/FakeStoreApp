package kim.young.fakestoreapp.android.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import kim.young.fakestoreapp.shared.domain.ProductDomainModel
import kim.young.fakestoreapp.shared.presentation.products.ProductsScreenSideEffects
import kim.young.fakestoreapp.shared.presentation.products.ProductViewModel
import org.koin.androidx.compose.inject

@Composable
fun DetailScreen(navController: NavController, productData: ProductDomainModel) {

    val productViewModel: ProductViewModel by inject()

    LaunchedEffect(key1 = 1) {
        Log.e("ProductScreen", "LaunchedEffect")
        productViewModel.onIntent(ProductsScreenSideEffects.GetDetail)
    }

    val state by productViewModel.state.collectAsState()


//    val viewModel: ProductsViewModel by inject()
//
//    LaunchedEffect(key1 = 1) {
////        viewModel.onIntent(ProductsScreenSideEffects.GetAllProducts)
//    }
//
//    val state by viewModel.state.collectAsState()

    Log.e("DetailScreen", "Reached")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {
//        TopAppBar {
//            Text(text = "Back", modifier = Modifier.clickable { navController.popBackStack() })
//            Text(text = "Fake Store App", fontSize = 24.sp, textAlign = TextAlign.Center)
//        }

    }


}

@Composable
fun ProductDetail(item: ProductDomainModel) {
    Column(
        modifier = Modifier.fillMaxSize(),

        ) {

        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .clip(MaterialTheme.shapes.large)
                .size(120.dp)
                .padding(12.dp),
            model = ImageRequest.Builder(
                LocalContext.current
            )
                .scale(Scale.FILL)
                .data(item.image)
                .crossfade(true)
                .build(),
            contentDescription = item.title,
            contentScale = ContentScale.FillBounds
        )

        item.title?.let {
            Text(
                text = it,
                color = MaterialTheme.colors.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 1.dp),
                overflow = TextOverflow.Ellipsis,
                maxLines = 2
            )
        }

        item.price?.let {
            Text(
                text = "$${item.price.toString()}",
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 10.sp,
                maxLines = 4
            )
        }

        item.description?.let {
            Text(
                text = it,
                color = MaterialTheme.colors.onSurface,
                maxLines = 5,
                fontSize = 10.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        horizontal = 18.dp
                    )
            )
        }


    }

}