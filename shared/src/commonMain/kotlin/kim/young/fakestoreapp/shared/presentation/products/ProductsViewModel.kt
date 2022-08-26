package kim.young.fakestoreapp.shared.presentation.products

import kim.young.fakestoreapp.shared.domain.usecase.GetProductListUseCase
import kim.young.fakestoreapp.shared.domain.usecase.GetProductByIdUseCase
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kim.young.fakestoreapp.shared.util.DataState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val getProductListUseCase: GetProductListUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase
) : ViewModel() {

    var state = MutableStateFlow(ProductsScreenState())

    init {
        getAllProducts()
    }

    fun onIntent(intent: ProductsScreenSideEffects) {

        when (intent) {
            is ProductsScreenSideEffects.GetAllProducts -> {
                getAllProducts()
            }
        }
    }



    private fun getAllProducts() {
        viewModelScope.launch {


            getProductListUseCase.invoke().collectLatest { dataState ->

                when (dataState) {
                    is DataState.Success -> {

                        state.emit(
                            state.value.copy(
                                isLoading = false,
                                isSuccess = true,
                                products = dataState.data ?: emptyList()
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

        }
    }
}



