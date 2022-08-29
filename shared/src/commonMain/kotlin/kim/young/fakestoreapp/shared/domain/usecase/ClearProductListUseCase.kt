package kim.young.fakestoreapp.shared.domain.usecase

import kim.young.fakestoreapp.shared.data.repository.ProductRepository

class ClearProductListUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke() = repository.clearProductListFromRealm()
}

