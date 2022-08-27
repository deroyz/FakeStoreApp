package kim.young.fakestoreapp.android.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import kim.young.fakestoreapp.android.presentation.util.DestinationScreen
import kim.young.fakestoreapp.android.presentation.util.NavParam
import kim.young.fakestoreapp.android.presentation.util.navigateTo
import kim.young.fakestoreapp.shared.domain.ProductDomainModel
import kim.young.fakestoreapp.shared.presentation.products.ProductsScreenSideEffects
import kim.young.fakestoreapp.shared.presentation.products.ProductViewModel
import org.koin.androidx.compose.inject

@Composable
fun ProductsScreen(navController: NavController) {

    val productViewModel: ProductViewModel by inject()

    Log.e("ProductScreen", "Reached")

    LaunchedEffect(key1 = 1) {
        Log.e("ProductScreen", "LaunchedEffect")
        productViewModel.onIntent(ProductsScreenSideEffects.GetAllProducts)
    }

    val state by productViewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {

        TopAppBar {
            Text(text = "Fake Store App", fontSize = 24.sp, textAlign = TextAlign.Center)
        }

        Row {
            SearchBar(
                hint = "Search...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
//                viewModel.onIntent((it))
            }
        }

        when {
            state.error.isError -> {
                Error(state)
            }
            state.isSuccess -> {

                LazyColumn(
                    contentPadding = PaddingValues(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val products = state.products
                    val isProductsEven = products.size % 2 == 0
                    val rowCount =
                        if (isProductsEven) {
                            products.size / 2
                        } else {
                            products.size / 2 + 1
                        }
                    items(rowCount) {
                        ProductRow(
                            navController = navController,
                            rowIndex = it,
                            products = products
                        )
                    }
                }
            }
            state.isLoading -> {
                Loading()
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf("") }
    var isHintDisplayed by remember { mutableStateOf(hint != "") }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            keyboardActions = KeyboardActions(onPrevious = {
                focusManager.clearFocus()
            }),
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),

            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = !(it.hasFocus || text.isNotEmpty())
                }
        )
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun ProductRow(
    navController: NavController,
    rowIndex: Int,
    products: List<ProductDomainModel>,
) {
    Column {
        Row {
            ProductEntry(
                item = products[rowIndex * 2],
                navController = navController,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(6.dp))

            if (products.size >= rowIndex * 2 + 2) {
                ProductEntry(
                    item = products[rowIndex * 2 + 1],
                    navController = navController,
                    modifier = Modifier.weight(1f),
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(3.dp))
    }
}

@Composable
fun ProductEntry(
    item: ProductDomainModel,
    navController: NavController,
    modifier: Modifier = Modifier,
) {

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .height(315.dp)
            .clickable {
                navigateTo(
                    navController,
                    DestinationScreen.Detail,
                    NavParam("product", item)
                )
            }
            .background(Color.White)
    ) {
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
}

