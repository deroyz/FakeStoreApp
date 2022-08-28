package kim.young.fakestoreapp.shared.domain.usecase

import kim.young.fakestoreapp.shared.data.repository.AbstractRepository


class GetProductListUseCase(private val repository: AbstractRepository) {
    operator fun invoke() = repository.getProductListFromRealm()
}


