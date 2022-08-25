package kim.young.fakestoreapp.shared

import android.os.Parcelable
import io.ktor.client.engine.android.*
import kim.young.fakestoreapp.shared.presentation.products.ProductsViewModel
import kotlinx.parcelize.Parcelize
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

actual fun platformModule() = module {
    single{
        Android.create()
    }

    viewModel{
        ProductsViewModel(get(), get())
    }
}

actual typealias CommonParcelize = Parcelize
actual typealias CommonParcelable = Parcelable
