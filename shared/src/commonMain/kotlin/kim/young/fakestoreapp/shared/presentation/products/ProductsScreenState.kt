package kim.young.fakestoreapp.shared.presentation.products

import kim.young.fakestoreapp.shared.domain.ProductDomainModel

data class ProductsScreenState(
    val isLoading: Boolean = true,
    val products: List<ProductDomainModel> = emptyList(),
    val error: Error = Error(),
    val isSuccess: Boolean = false,
)

data class Error(
    val isError: Boolean = false,
    val errorMessage: String = "Something went wrong."
)