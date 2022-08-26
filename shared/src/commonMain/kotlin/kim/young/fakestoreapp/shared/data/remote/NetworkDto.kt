package kim.young.fakestoreapp.shared.data.remote

import kim.young.fakestoreapp.shared.data.local.ProductDatabaseModel
import kim.young.fakestoreapp.shared.domain.ProductDomainModel
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

fun ProductNetworkModel.asDomainModel(): ProductDomainModel {
    return ProductDomainModel(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        image = image,
        rate = rating.rate,
        count = rating.count
    )

}

fun List<ProductNetworkModel>.asDomainModel(): List<ProductDomainModel> {
    return map {
        it.asDomainModel()
    }
}

fun ProductNetworkModel.asDatabaseModel(): ProductDatabaseModel {
    return ProductDatabaseModel().apply {
        id = this@asDatabaseModel.id
        title = this@asDatabaseModel.title
        price = this@asDatabaseModel.price
        description = this@asDatabaseModel.description
        category = this@asDatabaseModel.category
        image = this@asDatabaseModel.image
        rate = this@asDatabaseModel.rating.rate
        count = this@asDatabaseModel.rating.count
    }
}


fun List<ProductNetworkModel>.asDatabaseModel(): List<ProductDatabaseModel> {
    return map {
        it.asDatabaseModel()
    }
}
