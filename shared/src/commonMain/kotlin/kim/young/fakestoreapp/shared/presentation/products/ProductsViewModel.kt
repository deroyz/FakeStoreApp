package kim.young.fakestoreapp.shared.presentation.products

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kim.young.fakestoreapp.shared.data.remote.Category
import kim.young.fakestoreapp.shared.domain.usecase.GetProductListUseCase
import kim.young.fakestoreapp.shared.util.DataState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val getProductListUseCase: GetProductListUseCase,
) : ViewModel() {

    var state = MutableStateFlow(ProductsScreenState())
    val userIntent = Channel<ProductsIntent>(Channel.UNLIMITED)

    init {
        handleIntent()
//        viewModelScope.launch {
//        }
    }

    fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect { collector ->
                when (collector) {
                    is ProductsIntent.GetProductList -> getProductList()
                    is ProductsIntent.GetDetailProduct -> getDetailProduct()
                    is ProductsIntent.GetFilterList -> getFilterList()
                    is ProductsIntent.SearchProductListByName -> searchByName()
                    else -> {}
                }
            }
        }
    }

    fun updateFilterList(filter: Category) {

//        val isFilterUpdated = state.value.filterList.contains(filter)
//        val filter = state.value.filterList
//
//        viewModelScope.launch {
//            if (isFilterUpdated) {
//                state.emit(
//                    state.value.copy(
//                        filterList = state.value.filterList - filter,
//                        isSuccess = false,
//                    )
//                )
//            } else {
//                state.emit(
//                    state.value.copy(
//                        filterList = state.value.filterList + filter,
//                        isSuccess = false,
//                    )
//                )
//            }
//            println("===============>Updated Filter List${state.value.filterList}")
//        }
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

    private fun getFilterList() {
        val presentProductList = state.value.presentProductList
        val existingCategory: HashMap<String, String> = hashMapOf()

        presentProductList.forEach {
            val category = it.category.toString()
            existingCategory[category] = category
        }

        val filterList: List<String> = ArrayList(existingCategory.values)

        viewModelScope.launch {
            state.emit(
                state.value.copy(
                    isSuccess = false,
                    filterList = filterList
                )
            )
        }
    }

    private fun searchByName() {
        println("===============================================> Called SearchByName ")
        val name = state.value.searchProductName
        state.value.copy(isLoading = true)
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


    private fun applyFilters() {

//        val currentFilter = state.value.filterList
//
//
//        viewModelScope.launch {
//            f.emit(
//                state.value.copy(
//                    presentProductList = state.value.allProductList
//                )
//            )
//        }

    }


}



