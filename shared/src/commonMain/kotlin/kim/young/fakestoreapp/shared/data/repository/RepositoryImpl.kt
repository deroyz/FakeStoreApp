package kim.young.fakestoreapp.shared.data.repository

import kim.young.fakestoreapp.shared.data.local.AbstractRealmService
import kim.young.fakestoreapp.shared.data.local.asDomainModel
import kim.young.fakestoreapp.shared.data.remote.AbstractApiService
import kim.young.fakestoreapp.shared.data.remote.asDatabaseModel
import kim.young.fakestoreapp.shared.data.remote.asDomainModel
import kim.young.fakestoreapp.shared.domain.ProductDomainModel
import kim.young.fakestoreapp.shared.util.DataState
import kim.young.fakestoreapp.shared.util.ResponseHandler
import kotlinx.coroutines.flow.*

abstract class AbstractRepository {
    abstract fun getProductListFromRealm(): Flow<DataState<List<ProductDomainModel>>>
}

class RepositoryImpl(
    private val apiService: AbstractApiService,
    private val realmService: AbstractRealmService,
    private val responseHandler: ResponseHandler
) : AbstractRepository() {

    // Function which is supposed to be called only if no current product list exists in Realm database
    suspend fun getProductListFromApi(
        onError: suspend (message: DataState.CustomMessages) -> Unit,
        data: suspend (data: List<ProductDomainModel>) -> Unit
    ) {
        // Api response as a flow
        when (val dataState = apiService.getProductListFromApi()) {
            // Success receiving Api response
            is DataState.Success -> {
                val products = dataState.data?.asDatabaseModel() ?: emptyList()
                // Insert product list into database
                realmService.insertProductList(products)
                // Send product list retrieved from Realm database
                data(realmService.getProductListObj().asDomainModel() ?: emptyList()) }
            // Error occurred
            else -> {
                onError(dataState.error)
            }
        }
    }

    // Function retrieving product list from Realm database
    override fun getProductListFromRealm() = channelFlow<DataState<List<ProductDomainModel>>> {
        // Query to Realm Database
        val dm = realmService.getProductListObj().asDomainModel()
        // Check if retrieved list is empty
        if(dm?.isNotEmpty() == true) {
            send(responseHandler.handleSuccess(dm))
        } else {
            // Call Api if it is empty
            getProductListFromApi(
                { message -> send(responseHandler.handleException(message.message)) },
                { data -> send(responseHandler.handleSuccess(data)) }
            )
        }
    }
}

