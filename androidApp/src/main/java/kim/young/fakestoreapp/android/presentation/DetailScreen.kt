package kim.young.fakestoreapp.android.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import kim.young.fakestoreapp.shared.domain.ProductDomainModel
import kim.young.fakestoreapp.shared.presentation.products.ProductsIntent
import kim.young.fakestoreapp.shared.presentation.products.ProductsViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.inject

@Composable
fun DetailScreen(viewModel: ProductsViewModel, navController: NavController, productId: Int) {

    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = 1) {
        Log.e("DetailScreen", "LaunchedEffect detail ProductId: $productId")
        viewModel.handleIntent()
        viewModel.updateDetailProductId(productId)
        viewModel.userIntent.send(ProductsIntent.GetDetailProduct)
    }

    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        TopAppBar {
            val coroutineScope = rememberCoroutineScope()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Fake Store App", fontSize = 24.sp, textAlign = TextAlign.Center)
                Text(
                    text = "Back",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .clickable {
                        coroutineScope.launch {
                            viewModel.userIntent.send(ProductsIntent.GetProductList)
                        }
                        navController.popBackStack()
                    })
            }
        }
        Log.e("DetailScreen", "Updated product id is ${state.detailProductId}")
        when {
            state.error.isError -> {
                Error(state)
            }
            state.isSuccess -> {
                ProductDetail(item = state.detailProduct)
            }
            state.isLoading -> {
                Loading()
            }
        }
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
                .clip(MaterialTheme.shapes.large)
                .size(500.dp)
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
        Text(
            text = "${item.title} - $${item.price}",
            color = MaterialTheme.colors.onSurface,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 1.dp),
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.width(30.dp))
        Text(
            text = "Description",
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 1.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        )
        item.description?.let {
            Text(
                text = it,
                color = MaterialTheme.colors.onSurface,
                fontSize = 15.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 1.dp),
            )
        }
    }
}

