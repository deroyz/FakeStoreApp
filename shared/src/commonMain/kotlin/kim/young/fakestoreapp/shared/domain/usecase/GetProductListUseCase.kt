package kim.young.fakestoreapp.shared.domain.usecase

import kim.young.fakestoreapp.shared.data.repository.ProductRepository

class GetProductListUseCase(private val repository: ProductRepository) {
    operator fun invoke() = repository.getProductListFromDatabase()
}


