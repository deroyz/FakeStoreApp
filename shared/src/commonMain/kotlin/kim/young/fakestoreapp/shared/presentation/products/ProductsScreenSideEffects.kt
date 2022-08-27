package kim.young.fakestoreapp.shared.presentation.products

sealed class ProductsScreenSideEffects{
    object GetAllProducts : ProductsScreenSideEffects()
    object SearchProductByName : ProductsScreenSideEffects()
    object GetProductsByFilter: ProductsScreenSideEffects()
    object GetDetail : ProductsScreenSideEffects()
}
