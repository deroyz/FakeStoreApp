package kim.young.fakestoreapp.shared.data.repository

import kim.young.fakestoreapp.shared.data.local.AbstractRealmService
import kim.young.fakestoreapp.shared.data.local.ProductDatabaseModel
import kim.young.fakestoreapp.shared.data.local.asDomainModel
import kim.young.fakestoreapp.shared.data.remote.AbstractApiService
import kim.young.fakestoreapp.shared.data.remote.ProductNetworkModel
import kim.young.fakestoreapp.shared.data.remote.asDatabaseModel
import kim.young.fakestoreapp.shared.data.remote.asDomainModel
import kim.young.fakestoreapp.shared.domain.ProductDomainModel
import kim.young.fakestoreapp.shared.util.DataState
import kim.young.fakestoreapp.shared.util.ResponseHandler
import kotlinx.coroutines.flow.*

abstract class AbstractRepository {
    abstract fun getProductList(): Flow<DataState<List<ProductDomainModel>>>
    abstract fun getProductById(id: Int): Flow<DataState<out Any>>
}

class RepositoryImpl(
    private val apiService: AbstractApiService,
    private val realmService: AbstractRealmService,
    private val responseHandler: ResponseHandler
) : AbstractRepository() {

    suspend fun getProductListFromApi(
        onError: suspend (message: DataState.CustomMessages) -> Unit,
        data: suspend (data: List<ProductDomainModel>) -> Unit
    ) {
        flow { emit(apiService.getProductListFromApi()) }.collect() { dataState ->
            when (dataState) {

                is DataState.Success -> {
                    realmService.insertProductList(dataState.data?.asDatabaseModel() ?: emptyList())
                    data(dataState.data?.asDomainModel() ?: emptyList())
                }

                is DataState.Error -> {
                    onError(dataState.error)
                }

                else -> {
                    onError(dataState.error)
                }
            }
        }
    }

    override fun getProductList() =

        channelFlow<DataState<List<ProductDomainModel>>> {

            realmService.getProductList().collect() { resultsChange ->

                if (resultsChange.list.isEmpty()) {

                    println("====================================> List is Empty")
                    getProductListFromApi(
                        { message ->
                            send(responseHandler.handleException(message.message))
                        },
                        { data ->
                            println(data.toString())
                            send(responseHandler.handleSuccess(data))
                        }
                    )

                } else {
                    println("=====================================> List is full")
                    send(responseHandler.handleSuccess(resultsChange.list.asDomainModel()))
                }
            }
        }

    override fun getProductById(id: Int): Flow<DataState<out Any>> {

        return channelFlow {
            realmService.getProductById(id).collect() { result ->
                if (result.list.isNullOrEmpty()) {
                    getProductListFromApi(
                        { message ->
                            send(responseHandler.handleException(message.message))
                        },
                        { data ->
                            println(data.toString())
                            send(responseHandler.handleSuccess(data))
                        }
                    )
                } else {
                    send(responseHandler.handleSuccess(result.list.asDomainModel()))
                }
            }
        }
    }


}

