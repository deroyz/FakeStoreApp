package kim.young.fakestoreapp.shared.presentation

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kim.young.fakestoreapp.shared.domain.usecase.ClearProductListUseCase
import kim.young.fakestoreapp.shared.domain.usecase.GetProductListUseCase
import kim.young.fakestoreapp.shared.util.DataState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val getProductListUseCase: GetProductListUseCase,
    private val clearProductListDatabaseUseCase: ClearProductListUseCase
) : ViewModel() {

    var state = MutableStateFlow(ProductsState())
    val userIntent = Channel<ProductsIntent>(Channel.UNLIMITED)

    fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect { collector ->
                when (collector) {
                    is ProductsIntent.GetProductList -> getProductList()
                    is ProductsIntent.GetDetailProduct -> getDetailProduct()
                    is ProductsIntent.SearchProductListByName -> searchByName()
                    is ProductsIntent.GetFilterList -> getFilterList()
                    is ProductsIntent.ApplyNewFilter -> applyNewFilter()
                    is ProductsIntent.ClearProductListDatabase -> clearProductListDatabase()
                }
            }
        }
    }

    private fun clearProductListDatabase() {
        viewModelScope.launch {
            clearProductListDatabaseUseCase.invoke()
        }
    }

    fun updateFilterList(filter: String) {
        println("===============================================> Called updateFilterList filter: $filter")

        val currentFilterList = state.value.presentFilterList
        val isFilterUpdated = currentFilterList.contains(filter)

        viewModelScope.launch {
            if (isFilterUpdated) {
                state.emit(
                    state.value.copy(
                        isLoading = false,
                        presentFilterList = state.value.presentFilterList - filter
                    )
                )
            } else {
                state.emit(
                    state.value.copy(
                        isLoading = false,
                        presentFilterList = state.value.presentFilterList + filter
                    )
                )
            }
        }
        println("===============================================> Called updateFilterList filter: ${state.value.presentFilterList}")
    }

    private fun getProductList() {
        println("===============================================> Called getProductList")

        viewModelScope.launch {
            if (state.value.presentProductList.isEmpty()) {
                println("===============================================> Called getProductList, presentProductList Empty")

                getProductListUseCase.invoke().collectLatest { dataState ->
                    when (dataState) {
                        is DataState.Success -> {
                            state.emit(
                                state.value.copy(
                                    isLoading = false,
                                    isSuccess = true,
                                    allProductList = dataState.data ?: emptyList(),
                                    presentProductList = dataState.data ?: emptyList(),
                                )
                            )
                        }
                        is DataState.Error -> {
                            state.emit(
                                state.value.copy(
                                    isLoading = false,
                                    isSuccess = false,
                                    error = Error(true, "No internet connection"),
                                )
                            )
                        }
                        else -> {
                            state.emit(
                                state.value.copy(
                                    isLoading = false,
                                    isSuccess = false,
                                    error = Error(true, dataState.error.message),
                                )
                            )
                        }
                    }
                }
            } else {
                println("===============================================> Called getProductList, presentProductList Filled")

                state.emit(
                    state.value.copy(
                        isLoading = false,
                        isSuccess = true,
                    )
                )
            }
        }
    }

    fun updateSearchName(name: String) {
        println("===============================================> Called updateSearchName name: $name")

        viewModelScope.launch {
            state.emit(
                state.value.copy(
                    isLoading = false,
                    isSuccess = false,
                    searchProductName = name
                )
            )
        }
    }

    private fun searchByName() {
        println("===============================================> Called SearchByName ")

        applyNewFilter()
    }

    fun updateDetailProductId(id: Int) {
        println("===============================================> Called updateDetailProductId Id: $id")

        viewModelScope.launch {
            state.emit(
                state.value.copy(
                    isLoading = false,
                    isSuccess = false,
                    detailProductId = id
                )
            )
        }
    }

    fun getDetailProduct() {
        println("===============================================> Called getDetailProduct")

        val detailProductId = state.value.detailProductId
        viewModelScope.launch {
            val detailProduct =
                state.value.allProductList.last { it.id == detailProductId }
            state.emit(
                state.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    detailProduct = detailProduct
                )
            )
        }
    }

    private fun getFilterList() {
        println("===============================================> Called getCurrentFilterList")

        val allProductList = state.value.allProductList
        val presentProductList = state.value.presentProductList

        val allFilters: HashMap<String, String> = hashMapOf()
        val presentFilters: HashMap<String, String> = hashMapOf()

        allProductList.forEach {
            val category = it.category.toString()
            allFilters[category] = category
        }

        presentProductList.forEach {
            val category = it.category.toString()
            presentFilters[category] = category
        }

        val allFilterList: List<String> = ArrayList(allFilters.values)
        val presentFilterList: List<String> = ArrayList(presentFilters.values)

        viewModelScope.launch {
            state.emit(
                state.value.copy(
                    isSuccess = true,
                    presentFilterList = presentFilterList,
                    allFilterList = allFilterList
                )
            )
        }
        println("===============================================> Called getCurrentFilterList -> AllFilters: $allFilterList")
        println("===============================================> Called getCurrentFilterList -> CheckedFilters: $presentFilterList")
    }

    private fun applyNewFilter() {
        println("===============================================> Called applyNewFilter ")

        val name = state.value.searchProductName
        val checkedFilter = state.value.presentFilterList
        viewModelScope.launch {
            val allProductList = state.value.allProductList
            val filteredProductList = allProductList.filter { product ->
                checkedFilter.contains(product.category) &&  product.title?.contains(name, ignoreCase = true) == true
            }
            state.emit(
                state.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    presentProductList = filteredProductList
                )
            )
        }
    }

    fun onClickRefresh() {
        viewModelScope.launch {
            userIntent.send(ProductsIntent.ClearProductListDatabase)
            state.emit(ProductsState())
            println("===============================================> Called onClickRefresh List: ${state.value.presentFilterList}")
            println("===============================================> Called onClickRefresh List: ${state.value.allProductList}")
            userIntent.send(ProductsIntent.GetProductList)
        }
    }

}



