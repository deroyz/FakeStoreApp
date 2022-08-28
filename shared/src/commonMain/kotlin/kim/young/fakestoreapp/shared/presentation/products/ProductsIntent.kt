package kim.young.fakestoreapp.shared.presentation.products

sealed class ProductsIntent {
    // For products screen entry
    object GetProductList : ProductsIntent()

    // For search by name feature
    object SearchProductListByName : ProductsIntent()

    // For detail screen entry
    object GetDetailProduct : ProductsIntent()

    // For filter feature
    object GetFilterList : ProductsIntent()
    object ApplyFilter : ProductsIntent()
}
