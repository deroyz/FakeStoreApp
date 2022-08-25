package kim.young.fakestoreapp.shared.data.repository

import io.realm.kotlin.internal.platform.isFrozen
import kim.young.fakestoreapp.shared.data.local.AbstractRealmService
import kim.young.fakestoreapp.shared.data.local.asDomainModel
import kim.young.fakestoreapp.shared.data.remote.AbstractApiService
import kim.young.fakestoreapp.shared.data.remote.asDatabaseModel
import kim.young.fakestoreapp.shared.data.remote.map
import kim.young.fakestoreapp.shared.domain.ProductDomainModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class AbstractRepository {
    abstract suspend fun cacheProductListFromApi()
    abstract fun getAllProductsFromRealm(): Flow<List<ProductDomainModel>>
}

class RepositoryImpl(
    private val ApiService: AbstractApiService,
    private val RealmService: AbstractRealmService
) : AbstractRepository() {

    override suspend fun cacheProductListFromApi() {

        CoroutineScope(Dispatchers.Default).launch {

            ApiService.getProductsFromApi().data!!.asIterable().asFlow().onCompletion {
                RealmService.insertProductList()
            }
        }
    }

    override fun getAllProductsFromRealm(): Flow<List<ProductDomainModel>> {
        return channelFlow<List<ProductDomainModel>> {
            RealmService.getAllProducts().collect() { resultsChange ->
                resultsChange.list.asDomainModel()
            }
        }
    }

}

