package kim.young.fakestoreapp.shared.presentation.products

sealed class ProductsIntent {
    // Products screen entry
    object GetProductList : ProductsIntent()

    // Search by name
    object SearchProductListByName : ProductsIntent()
    object RefreshSearchWord: ProductsIntent()

    // Detail screen entry
    object GetDetailProduct : ProductsIntent()

    // Filter feature
    object GetFilterList : ProductsIntent()
    object ApplyNewFilter : ProductsIntent()
}