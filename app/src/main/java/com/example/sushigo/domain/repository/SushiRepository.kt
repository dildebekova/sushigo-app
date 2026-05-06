package com.example.sushigo.domain.repository

import com.example.sushigo.data.local.entity.CartEntity
import com.example.sushigo.data.local.entity.UserEntity
import com.example.sushigo.domain.model.Product
import com.example.sushigo.domain.model.Restaurant
import kotlinx.coroutines.flow.Flow

interface SushiRepository {
    fun getAllProducts(): Flow<List<Product>>
    fun getProductsByCategory(category: String): Flow<List<Product>>
    suspend fun getProductById(productId: Int): Product?
    fun getCartItems(): Flow<List<CartEntity>>
    suspend fun addToCart(product: Product)
    suspend fun updateCartItem(item: CartEntity)
    suspend fun removeFromCart(cartItem: CartEntity)
    fun getRestaurants(): Flow<List<Restaurant>>

    // User operations
    suspend fun registerUser(name: String, phone: String)
    suspend fun loginUser(name: String): UserEntity?
}
