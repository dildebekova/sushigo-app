package com.example.sushigo.data.repository

import com.example.sushigo.data.local.dao.SushiDao
import com.example.sushigo.data.local.entity.CartEntity
import com.example.sushigo.data.local.entity.toDomain
import com.example.sushigo.domain.model.Product
import com.example.sushigo.domain.model.Restaurant
import com.example.sushigo.domain.repository.SushiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SushiRepositoryImpl @Inject constructor(
    private val sushiDao: SushiDao
) : SushiRepository {

    override fun getAllProducts(): Flow<List<Product>> {
        return sushiDao.getAllProducts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getProductsByCategory(category: String): Flow<List<Product>> {
        return sushiDao.getProductsByCategory(category).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getProductById(productId: Int): Product? {
        return sushiDao.getProductById(productId)?.toDomain()
    }

    override fun getCartItems(): Flow<List<CartEntity>> {
        return sushiDao.getCartItems()
    }

    override suspend fun addToCart(product: Product) {
        val existingItem = sushiDao.getCartItemByProduct(product.id)
        if (existingItem != null) {
            sushiDao.updateCartItem(existingItem.copy(quantity = existingItem.quantity + 1))
        } else {
            sushiDao.addToCart(
                CartEntity(
                    productId = product.id,
                    productName = product.name,
                    price = product.price,
                    image = product.image,
                    quantity = 1
                )
            )
        }
    }

    override suspend fun updateCartItem(item: CartEntity) {
        if (item.quantity <= 0) {
            sushiDao.removeFromCart(item)
        } else {
            sushiDao.updateCartItem(item)
        }
    }

    override suspend fun removeFromCart(cartItem: CartEntity) {
        sushiDao.removeFromCart(cartItem)
    }

    override fun getRestaurants(): Flow<List<Restaurant>> {
        return sushiDao.getRestaurants().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
