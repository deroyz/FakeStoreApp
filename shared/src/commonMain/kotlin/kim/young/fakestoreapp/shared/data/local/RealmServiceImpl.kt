package kim.young.fakestoreapp.shared.data.local

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.ResultsChange
import kim.young.fakestoreapp.shared.data.remote.ProductNetworkModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

interface AbstractRealmService {
    suspend fun getAllProducts(): Flow<ResultsChange<ProductDatabaseModel>>
    suspend fun insertProductList(product: ProductDatabaseModel)
}

class RealmServiceImpl(private val realm: Realm) : AbstractRealmService {

    override suspend fun getAllProducts() = realm.query<ProductDatabaseModel>().find().asFlow()

    override suspend fun insertProductList(product: ProductDatabaseModel) {
        return realm.writeBlocking {
            copyToRealm(
                product
            )
        }
    }
}

