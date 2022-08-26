package kim.young.fakestoreapp.shared.domain.usecase

import kim.young.fakestoreapp.shared.data.repository.AbstractRepository
import kim.young.fakestoreapp.shared.util.asCommonFlow

class GetProductByIdUseCase(private val repository: AbstractRepository){

    operator fun invoke(id: Int) = repository.getProductById(id).asCommonFlow()

}
