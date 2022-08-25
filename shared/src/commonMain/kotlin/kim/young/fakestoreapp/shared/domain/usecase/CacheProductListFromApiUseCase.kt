package kim.young.fakestoreapp.shared.domain.usecase

import kim.young.fakestoreapp.shared.data.repository.AbstractRepository

class CacheProductListFromApiUseCase(
    private val repository: AbstractRepository
) {

    suspend operator fun invoke() {
       repository.cacheProductListFromApi()
    }

}
