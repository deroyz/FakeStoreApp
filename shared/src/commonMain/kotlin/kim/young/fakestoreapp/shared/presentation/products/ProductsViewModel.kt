package kim.young.fakestoreapp.shared.presentation.products

import kim.young.fakestoreapp.shared.domain.usecase.CacheProductListFromApiUseCase
import kim.young.fakestoreapp.shared.domain.usecase.GetAllProductsFromRealmUseCase
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val getAllProductsFromRealmUseCase: GetAllProductsFromRealmUseCase,
    private val cacheProductListFromApiUseCase: CacheProductListFromApiUseCase
) : ViewModel() {

    var state = MutableStateFlow(ProductsScreenState())

    init{
        Napier.e { "ViewModel!"}
        cacheProductList()
        getAllProducts()
    }

    fun onIntent(intent: ProductsScreenSideEffects) {

        when (intent) {
            is ProductsScreenSideEffects.GetAllProducts -> {
                getAllProducts()
            }
            is ProductsScreenSideEffects.CacheProductList -> {
                cacheProductList()
            }
        }
    }

    private fun getAllProducts() {

        viewModelScope.launch{
            getAllProductsFromRealmUseCase.invoke().collectLatest(){

            }
        }
    }

    private fun cacheProductList() {
        viewModelScope.launch {
            cacheProductListFromApiUseCase.invoke()
        }
    }

}