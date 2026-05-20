package com.example.sushigo.data.local.dao

import androidx.room.*
import com.example.sushigo.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SushiDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int

    @Query("SELECT * FROM products WHERE category = :category")
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: Int): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    // Cart operations
    @Query("SELECT * FROM cart WHERE userName = :userName")
    fun getCartItems(userName: String): Flow<List<CartEntity>>

    @Query("SELECT * FROM cart WHERE productId = :productId AND userName = :userName")
    suspend fun getCartItemByProduct(productId: Int, userName: String): CartEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(cartEntity: CartEntity)

    @Update
    suspend fun updateCartItem(cartEntity: CartEntity)

    @Delete
    suspend fun removeFromCart(cartEntity: CartEntity)

    @Query("DELETE FROM cart WHERE userName = :userName")
    suspend fun clearCart(userName: String)

    // Restaurant operations
    @Query("SELECT * FROM restaurants")
    fun getRestaurants(): Flow<List<RestaurantEntity>>

    @Query("SELECT COUNT(*) FROM restaurants")
    suspend fun getRestaurantCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurants(restaurants: List<RestaurantEntity>)

    // User operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE name = :name LIMIT 1")
    suspend fun getUserByName(name: String): UserEntity?

    // Order operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Query("SELECT * FROM orders WHERE userName = :userName ORDER BY timestamp DESC")
    fun getOrdersByUserName(userName: String): Flow<List<OrderEntity>>
}
