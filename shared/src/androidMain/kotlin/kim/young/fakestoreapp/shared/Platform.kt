package kim.young.fakestoreapp.shared

import io.ktor.client.engine.android.*
import kim.young.fakestoreapp.shared.presentation.ProductsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

actual fun platformModule() = module {
    single{
        Android.create()
    }

    viewModel{
        ProductsViewModel(get())
    }
}

actual typealias Parcelize = kotlinx.parcelize.Parcelize
actual typealias Parcelable = android.os.Parcelable
