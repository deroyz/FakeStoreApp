package kim.young.fakestoreapp.shared.data.local

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.query.RealmResults
import kim.young.fakestoreapp.shared.data.remote.ProductNetworkModel
import kim.young.fakestoreapp.shared.domain.ProductDomainModel
import kim.young.fakestoreapp.shared.util.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

interface AbstractRealmService {
    suspend fun insertProductList(products: List<ProductDatabaseModel>)

    fun getProductList(): Flow<ResultsChange<ProductDatabaseModel>>

    fun getProductListObj(): RealmResults<ProductDatabaseModel>
}

class RealmServiceImpl(private val realm: Realm) : AbstractRealmService {

    // Writing data into Realm database
    override suspend fun insertProductList(products: List<ProductDatabaseModel>) {
        realm.writeBlocking {
            val queries = query<ProductDatabaseModel>().find()
            delete(queries)

            products.forEach { product ->
                copyToRealm(
                    product
                )
            }
        }
    }

    // Fetching data from Realm database
    override fun getProductList() = realm.query<ProductDatabaseModel>().find().asFlow()

    override fun getProductListObj() = realm.query<ProductDatabaseModel>().find()


}

