package kim.young.fakestoreapp.shared.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import kim.young.fakestoreapp.shared.util.DataState

interface FakeStoreApi {
    suspend fun getProductListFromApi(): DataState<List<ProductNetworkModel>>
}

class DefaultFakeStoreApi(
    private val httpClient: HttpClient,
    private val baseUrl: String
) : FakeStoreApi {

    // Api call with exception handling
    override suspend fun getProductListFromApi(): DataState<List<ProductNetworkModel>> {
        return try {
            DataState.Success(httpClient.get(baseUrl).body())
        } catch (e: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${e.response.status.description}")
            DataState.Error(DataState.CustomMessages.ExceptionMessage(e.response.status.description))
        } catch (e: ClientRequestException) {
            // 4xx - responses
            println("Error: ${e.response.status.description}")
            DataState.Error(DataState.CustomMessages.ExceptionMessage(e.response.status.description))
        } catch (e: ServerResponseException) {
            // 5xx - responses
            DataState.Error(DataState.CustomMessages.ExceptionMessage(e.response.status.description))
        } catch (e: Exception) {
            println("Error: ${e.message}")
            DataState.Error(
                DataState.CustomMessages.ExceptionMessage(
                    e.message ?: "Something went wrong"
                )
            )
        }
    }
}



