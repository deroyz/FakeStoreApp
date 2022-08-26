package kim.young.fakestoreapp.shared.data.local

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import kim.young.fakestoreapp.shared.data.remote.ProductNetworkModel
import kim.young.fakestoreapp.shared.util.DataState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

interface AbstractRealmService {
    suspend fun insertProductList(products: List<ProductDatabaseModel>)
    fun getProductList(): Flow<ResultsChange<ProductDatabaseModel>>
    fun getProductById(id: Int): Flow<ResultsChange<ProductDatabaseModel>>
}

class RealmServiceImpl(private val realm: Realm) : AbstractRealmService {

    override suspend fun insertProductList(products: List<ProductDatabaseModel>) {
        return products.forEach { product ->
            realm.writeBlocking {
                copyToRealm(
                    product
                )
            }
        }
    }

    override fun getProductList() = realm.query<ProductDatabaseModel>().find().asFlow()

    override fun getProductById(id: Int): Flow<ResultsChange<ProductDatabaseModel>> =
        realm.query<ProductDatabaseModel>("id == $id").find().asFlow()



}

