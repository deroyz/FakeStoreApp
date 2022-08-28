package kim.young.fakestoreapp.shared.di


import kim.young.fakestoreapp.shared.data.local.AbstractRealmService
import kim.young.fakestoreapp.shared.data.local.ProductDatabaseModel
import kim.young.fakestoreapp.shared.data.local.RealmServiceImpl
import kim.young.fakestoreapp.shared.data.remote.AbstractApiService
import kim.young.fakestoreapp.shared.data.remote.ApiServiceImpl
import kim.young.fakestoreapp.shared.data.repository.AbstractRepository
import kim.young.fakestoreapp.shared.data.repository.RepositoryImpl
import kim.young.fakestoreapp.shared.domain.usecase.GetProductListUseCase
import kim.young.fakestoreapp.shared.util.ResponseHandler
import kim.young.fakestoreapp.shared.platformModule
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module


fun initKoin(
    enableNetworkLogs: Boolean = false,
    baseUrl: String,
    appDeclaration: KoinAppDeclaration = {}
) =
    startKoin {
        appDeclaration()
        modules(
            getRepositoryModule(enableNetworkLogs = enableNetworkLogs, baseUrl),
            platformModule(),
            helperModule,
            useCaseModule,
        )
    }


// called by iOS etc
fun initKoin(baseUrl: String) = initKoin(enableNetworkLogs = true, baseUrl) {}


fun getRepositoryModule(enableNetworkLogs: Boolean, baseUrl: String) = module {

    // Http Client
    single {
        createHttpClient(
            get(),
            get(),
            enableNetworkLogs = enableNetworkLogs
        )
    }

    single { createJson() }

    // Realm Module
    single { createRealmDatabase() }

    // Data Structure Module (Realm + Ktor + Repository)
    single<AbstractRealmService> {
        RealmServiceImpl(get())
    }
    single<AbstractApiService> {
        ApiServiceImpl(get(), baseUrl)
    }
    single<AbstractRepository> {
        RepositoryImpl(get(), get(), get())
    }
}

// UseCase Module
val useCaseModule = module {
    single { GetProductListUseCase(get()) }
}

// ResponseHandle Module
val helperModule = module {
    single { ResponseHandler() }
}

// Build function for Realm
fun createRealmDatabase(): Realm {
    val configuration =
        RealmConfiguration.Builder(schema = setOf(ProductDatabaseModel::class)).build()
    return Realm.open(configuration)
}

// Build functions for Ktor
fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

fun createHttpClient(
    httpClientEngine: HttpClientEngine,
    json: Json,
    enableNetworkLogs: Boolean
) =
    HttpClient(httpClientEngine) {


        install(ContentNegotiation) {
            json(json)
        }
        if (enableNetworkLogs) {
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
        }
    }