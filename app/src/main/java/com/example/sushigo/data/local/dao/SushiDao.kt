package com.example.sushigo.data.local.dao

import androidx.room.*
import com.example.sushigo.data.local.entity.CartEntity
import com.example.sushigo.data.local.entity.ProductEntity
import com.example.sushigo.data.local.entity.RestaurantEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SushiDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE category = :category")
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: Int): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    // Cart operations
    @Query("SELECT * FROM cart")
    fun getCartItems(): Flow<List<CartEntity>>

    @Query("SELECT * FROM cart WHERE productId = :productId")
    suspend fun getCartItemByProduct(productId: Int): CartEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(cartEntity: CartEntity)

    @Update
    suspend fun updateCartItem(cartEntity: CartEntity)

    @Delete
    suspend fun removeFromCart(cartEntity: CartEntity)

    @Query("DELETE FROM cart")
    suspend fun clearCart()

    // Restaurant operations
    @Query("SELECT * FROM restaurants")
    fun getRestaurants(): Flow<List<RestaurantEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRestaurants(restaurants: List<RestaurantEntity>)
}
