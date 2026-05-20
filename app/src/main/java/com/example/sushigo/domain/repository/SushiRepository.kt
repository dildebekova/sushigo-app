package com.example.sushigo.domain.repository

import com.example.sushigo.domain.model.*
import kotlinx.coroutines.flow.Flow

interface SushiRepository {
    fun getAllProducts(): Flow<List<Product>>
    fun getProductsByCategory(category: String): Flow<List<Product>>
    suspend fun getProductById(productId: Int): Product?
    
    fun getCartItems(userName: String): Flow<List<CartItem>>
    suspend fun addToCart(product: Product, userName: String)
    suspend fun updateCartItem(item: CartItem)
    suspend fun removeFromCart(item: CartItem)
    suspend fun clearCart(userName: String)
    
    fun getRestaurants(): Flow<List<Restaurant>>

    // User operations
    suspend fun registerUser(name: String, phone: String, password: String)
    suspend fun loginUser(name: String, password: String): User?

    // Order operations
    suspend fun placeOrder(order: Order)
    fun getOrdersByUserName(userName: String): Flow<List<Order>>
}
