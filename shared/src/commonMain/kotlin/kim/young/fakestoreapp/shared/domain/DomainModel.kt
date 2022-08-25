package kim.young.fakestoreapp.shared.domain

import kim.young.fakestoreapp.shared.CommonParcelable
import kim.young.fakestoreapp.shared.CommonParcelize


@CommonParcelize
data class ProductDomainModel(
    val id: Int? = 0,
    val title: String? = "",
    val price: Double? = 0.0,
    val description: String? = "",
    val category: String? = "",
    val image: String? = "",
    val rate: Double? = 0.0,
    var count: Int? = 0,

) : CommonParcelable {
    constructor(): this(
        id = 0,
        title = "",
        price = 0.0,
        description = "",
        category = "",
        image = "",
        rate = 0.0,
        count = 0,
    )
}

