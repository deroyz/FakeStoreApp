package kim.young.fakestoreapp.shared.domain.usecase

import kim.young.fakestoreapp.shared.data.repository.AbstractRepository
import kim.young.fakestoreapp.shared.util.ResponseHandler

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class GetAllProductsFromRealmUseCase(
    private val repository: AbstractRepository,
) {
    operator fun invoke() = flow {
        val response = repository.getAllProductsFromRealm()
        emit(response)
    }
}


