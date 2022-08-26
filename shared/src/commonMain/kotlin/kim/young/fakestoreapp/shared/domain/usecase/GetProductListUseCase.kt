package kim.young.fakestoreapp.shared.domain.usecase

import kim.young.fakestoreapp.shared.data.repository.AbstractRepository
import kim.young.fakestoreapp.shared.util.asCommonFlow
import kotlinx.coroutines.flow.flowOn


class GetProductListUseCase(private val repository: AbstractRepository) {

    operator fun invoke() = repository.getProductList().asCommonFlow()

}


