package kim.young.fakestoreapp.shared.data.remote

import kim.young.fakestoreapp.shared.data.local.ProductDatabaseModel
import kotlinx.serialization.Serializable

@Serializable
data class NetworkDto(
    val products: List<ProductNetworkModel>
)

@Serializable
data class ProductNetworkModel(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating
)

@Serializable
data class Rating(
    val rate: Double,
    val count: Int
)

fun ProductNetworkModel.map(): ProductDatabaseModel{
    return ProductDatabaseModel().apply{
        id = this@map.id
        title = this@map.title
        price = this@map.price
        description = this@map.description
        category = this@map.category
        image = this@map.image
        rate = this@map.rating.rate
        count = this@map.rating.count
    }
}


fun List<ProductNetworkModel>.asDatabaseModel(): List<ProductDatabaseModel> {
    return map {
        it.map()
    }
}
