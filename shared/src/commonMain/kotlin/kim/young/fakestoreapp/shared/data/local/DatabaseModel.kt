package kim.young.fakestoreapp.shared.data.local

import io.realm.kotlin.types.RealmObject
import kim.young.fakestoreapp.shared.domain.ProductDomainModel

class ProductDatabaseModel:RealmObject {
    var id: Int = 0
    var title: String = ""
    var price: Double = 0.0
    var description: String = ""
    var category: String = ""
    var image: String = ""
    var rate: Double = 0.0
    var count: Int = 0
}

fun ProductDatabaseModel.asDomainModel() = ProductDomainModel(
    id = id,
    title = title,
    price = price,
    description = description,
    category = category,
    image = image,
    rate = rate,
    count = count
)

fun List<ProductDatabaseModel>.asDomainModel(): List<ProductDomainModel>? {
    return map {
        it.asDomainModel()
    }
}
