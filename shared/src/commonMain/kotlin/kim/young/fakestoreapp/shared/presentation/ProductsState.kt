package kim.young.fakestoreapp.shared.presentation

import kim.young.fakestoreapp.shared.domain.ProductDomainModel

data class ProductsState(

    val allFilterList: List<String> = emptyList(),
    val presentFilterList: List<String> = emptyList(),

    val searchProductName: String = "",
    val detailProductId: Int = -1,

    val detailProduct: ProductDomainModel = ProductDomainModel(),
    val allProductList: List<ProductDomainModel> = emptyList(),
    val presentProductList: List<ProductDomainModel> = emptyList(),

    val isLoading: Boolean = true,
    val isSuccess: Boolean = false,
    val error: Error = Error()
)

data class Error(val isError: Boolean = false, val errorMessage: String = "Something went wrong.")
