package com.example.sushigo.domain.usecase

import com.example.sushigo.domain.model.Product
import com.example.sushigo.domain.repository.SushiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsByCategoryUseCase @Inject constructor(
    private val repository: SushiRepository
) {
    operator fun invoke(category: String): Flow<List<Product>> {
        return repository.getProductsByCategory(category)
    }
}
