package kim.young.fakestoreapp.shared.presentation

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kim.young.fakestoreapp.shared.domain.usecase.GetProductListUseCase
import kim.young.fakestoreapp.shared.util.DataState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val getProductListUseCase: GetProductListUseCase,
) : ViewModel() {

    var state = MutableStateFlow(ProductsState())
    val userIntent = Channel<ProductsIntent>(Channel.UNLIMITED)

    fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect { collector ->
                when (collector) {
                    is ProductsIntent.GetProductList -> getProductList()
                    is ProductsIntent.GetDetailProduct -> getDetailProduct()
                    is ProductsIntent.GetFilterList -> getFilterList()
                    is ProductsIntent.SearchProductListByName -> searchByName()
                    is ProductsIntent.ApplyNewFilter -> applyNewFilter()
                    else->{}
                }
            }
        }
    }

    fun updateFilterList(filter: String) {
        println("===============================================> Called updateFilterList filter: $filter")

        val currentFilterList = state.value.presentFilterList
        val isFilterUpdated = currentFilterList.contains(filter)
        state.value.copy(
            isLoading = true,
            isSuccess = false
        )

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
            state.value.copy(
                isLoading = true,
                isSuccess = false
            )
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
                                    error = Error(true, dataState.error.message),
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

        val name = state.value.searchProductName
        state.value.copy(
            isLoading = true,
            isSuccess = false
        )
        viewModelScope.launch {
            val allProductList = state.value.allProductList
            val presentProductList = allProductList.asFlow().filter {
                it.title?.contains(name, ignoreCase = true) == true
            }.toList()

            state.emit(
                state.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    presentProductList = presentProductList
                )
            )
        }
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

        state.value.copy(
            isLoading = true,
            isSuccess = false
        )

        val detailProductId = state.value.detailProductId

        viewModelScope.launch {
            val detailProduct =
                state.value.allProductList.asFlow().filter { it.id == detailProductId }.last()
            state.emit(
                state.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    detailProduct = detailProduct
                )
            )
        }
        /*viewModelScope.launch {

            state.emit(state.value.copy(
                presentProductList = state.value.allProductList.filter {
                    it.title?.contains(name, ignoreCase = true) == true
                }
            ))
        }*/
    }

    private fun refreshSearch() {
        viewModelScope.launch {
            state.emit(
                state.value.copy(
                    isLoading = true,
                    isSuccess = false,
                    searchProductName = ""
                )
            )
        }
    }

    private fun getFilterList() {
        println("===============================================> Called getCurrentFilterList")

        state.value.copy(
            isLoading = true,
            isSuccess = false,
        )

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

        val checkedFilter = state.value.presentFilterList
        state.value.copy(
            isLoading = true,
            isSuccess = false
        )
        viewModelScope.launch {
            val presentProductList = state.value.presentProductList
            val filteredProductList = presentProductList.asFlow().filter { product ->
                checkedFilter.contains(product.category)
            }.toList()

            state.emit(
                state.value.copy(
                    isLoading = false,
                    isSuccess = true,
                    presentProductList = filteredProductList
                )
            )
        }
    }

}



