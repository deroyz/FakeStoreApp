package kim.young.fakestoreapp.android

import android.app.Application
import kim.young.fakestoreapp.shared.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class FakeStoreApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin(baseUrl = "https://fakestoreapi.com/products", enableNetworkLogs = BuildConfig.DEBUG) {
            androidContext(this@FakeStoreApplication)
            modules(
                listOf(module{/**
                     * android specific modules
                     */
                })
            )
        }
    }
}