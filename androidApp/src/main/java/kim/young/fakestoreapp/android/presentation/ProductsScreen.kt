package kim.young.fakestoreapp.android.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
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
import kim.young.fakestoreapp.android.R
import kim.young.fakestoreapp.android.presentation.util.DestinationScreen
import kim.young.fakestoreapp.shared.domain.ProductDomainModel
import kim.young.fakestoreapp.shared.presentation.products.ProductsIntent
import kim.young.fakestoreapp.shared.presentation.products.ProductsViewModel
import kim.young.fakestoreapp.shared.presentation.products.ProductsState
import kotlinx.coroutines.launch

@Composable
fun ProductsScreen(viewModel: ProductsViewModel, navController: NavController) {

    val state by viewModel.state.collectAsState()

    Log.e("ProductScreen", "Reached")

    LaunchedEffect(true) {
        Log.e("ProductScreen", "LaunchedEffect")
        viewModel.handleIntent()
        viewModel.userIntent.send(ProductsIntent.GetProductList)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
    ) {

        TopAppBar {
            Text(text = "Fake Store App", fontSize = 24.sp, textAlign = TextAlign.Center)
        }

        Row(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .size(50.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val openDialog = remember { mutableStateOf(false) }
            val coroutineScope = rememberCoroutineScope()

            SearchBar(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterVertically)
                    .padding(start = 10.dp, end = 10.dp),
                viewModel = viewModel,
                hint = "Search by name..."
            ) {
                viewModel.updateSearchName(it)
                coroutineScope.launch {
                    viewModel.userIntent.send(ProductsIntent.SearchProductListByName)
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable {
                        openDialog.value = true
                        coroutineScope.launch {
                            if(state.searchProductName.isNotBlank()){
                                viewModel.userIntent.send(ProductsIntent.RefreshSearchWord)
                            }
                            viewModel.userIntent.send(ProductsIntent.GetFilterList)
                        }
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_filter_list_24),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
                if (openDialog.value) {
                    AlertDialog(
                        modifier = Modifier
                            .height(300.dp),
                        onDismissRequest = { openDialog.value = false },
                        title = { Text(text = "Filter Products", fontWeight = FontWeight.Bold) },
                        text = {
                            Column(

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.7f)
                            ) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                ) {
                                    val allFilterList = state.allFilterList
                                    items(allFilterList) { filter ->
                                        SingleFilterRow(filter = filter, state = state) { filter ->
                                            viewModel.updateFilterList(filter)
                                        }
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            Box(modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(10.dp)
                                .clickable {
                                    coroutineScope.launch {
                                        viewModel.userIntent.send(ProductsIntent.ApplyNewFilter)
                                    }
                                    openDialog.value = false
                                }) {
                                Text("Ok")
                            }
                        },
                    )
                }
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
                    val products = state.presentProductList

                    Log.e("Products Screen", "LazyColumn Item List${products}")

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
fun SingleFilterRow(
    filter: String,
    state: ProductsState,
    filterChecked: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(30.dp)
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        val filterState = remember { mutableStateOf(state.presentFilterList.contains(filter))}
        Text(text = filter)
        Checkbox(
            checked = filterState.value,
            onCheckedChange = {
                filterState.value = it
                filterChecked(filter)
            }
        )
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    viewModel: ProductsViewModel,
    onSearch: (String) -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    var text by remember { mutableStateOf(viewModel.state.value.searchProductName) }
    var isHintDisplayed by remember { mutableStateOf(hint != "") }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            keyboardActions = KeyboardActions(
                onPrevious = { focusManager.clearFocus() },
            ),
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
                navController.navigate(DestinationScreen.Detail.createRoute(item.id))
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

