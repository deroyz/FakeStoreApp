package kim.young.fakestoreapp.shared.di

import kim.young.fakestoreapp.shared.MainDispatcher
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {

    single { MainDispatcher() }
}
