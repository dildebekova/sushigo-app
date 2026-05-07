package com.example.sushigo.data.repository

import com.example.sushigo.data.local.dao.SushiDao
import com.example.sushigo.data.local.entity.*
import com.example.sushigo.domain.model.Product
import com.example.sushigo.domain.model.Restaurant
import com.example.sushigo.domain.repository.SushiRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SushiRepositoryImpl @Inject constructor(
    private val sushiDao: SushiDao
) : SushiRepository {

    override fun getAllProducts(): Flow<List<Product>> {
        return sushiDao.getAllProducts()
            .onStart { seedDatabaseIfNeeded() }
            .map { entities -> entities.map { it.toDomain() } }
    }

    override fun getProductsByCategory(category: String): Flow<List<Product>> {
        return sushiDao.getProductsByCategory(category)
            .onStart { seedDatabaseIfNeeded() }
            .map { entities -> entities.map { it.toDomain() } }
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

    override suspend fun clearCart() {
        sushiDao.clearCart()
    }

    override fun getRestaurants(): Flow<List<Restaurant>> {
        return sushiDao.getRestaurants()
            .onStart { seedDatabaseIfNeeded() }
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun registerUser(name: String, phone: String) {
        sushiDao.insertUser(UserEntity(name, phone))
    }

    override suspend fun loginUser(name: String): UserEntity? {
        return sushiDao.getUserByName(name)
    }

    override suspend fun placeOrder(order: OrderEntity) {
        sushiDao.insertOrder(order)
    }

    override fun getOrdersByUserName(userName: String): Flow<List<OrderEntity>> {
        return sushiDao.getOrdersByUserName(userName)
    }

    private suspend fun seedDatabaseIfNeeded() {
        if (sushiDao.getProductCount() == 0) {
            val products = listOf(
                // 🔥 Combo Sets
                ProductEntity(1, "Family Combo", 1800.0, "https://images.unsplash.com/photo-1559339352-11d035aa65de?w=500", "Combo Sets", "2 pizzas + 2 sushi rolls + 1.5L drink"),
                ProductEntity(2, "Party Set", 2500.0, "https://images.unsplash.com/photo-1615361200141-f4500043e95d?w=500", "Combo Sets", "3 pizzas + 4 sushi rolls + snacks"),
                ProductEntity(3, "Couple Combo", 1200.0, "https://images.unsplash.com/photo-1579871494447-9811cf80d66c?w=500", "Combo Sets", "1 pizza + 2 sushi rolls + 2 drinks"),
                ProductEntity(4, "Student Combo", 850.0, "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500", "Combo Sets", "small pizza + 1 sushi roll + drink"),
                
                // 🍣 Sushi Rolls
                ProductEntity(5, "California Roll", 350.0, "https://images.unsplash.com/photo-1579584425555-c3ce17fd4351?w=500", "Sushi Rolls", "Crab, avocado, cucumber"),
                ProductEntity(6, "Philadelphia Roll", 400.0, "https://images.unsplash.com/photo-1611143669185-af224c5e3252?w=500", "Sushi Rolls", "Salmon, cream cheese, cucumber"),
                ProductEntity(7, "Tempura Roll", 420.0, "https://images.unsplash.com/photo-1617196034796-73dfa7b1fd56?w=500", "Sushi Rolls", "Fried roll with shrimp"),
                ProductEntity(8, "Dragon Roll", 480.0, "https://images.unsplash.com/photo-1583623025817-d180a2221d0a?w=500", "Sushi Rolls", "Eel, avocado, unagi sauce"),
                ProductEntity(9, "Spicy Tuna Roll", 390.0, "https://images.unsplash.com/photo-1562158074-2111958b455c?w=500", "Sushi Rolls", "Tuna, spicy mayo"),
                
                // 🍕 Pizza
                ProductEntity(10, "Margherita", 500.0, "https://images.unsplash.com/photo-1574071318508-1cdbad80ad38?w=500", "Pizza", "Tomato sauce, mozzarella, basil"),
                ProductEntity(11, "Pepperoni", 650.0, "https://images.unsplash.com/photo-1628840042765-356cda07504e?w=500", "Pizza", "Tomato sauce, pepperoni, mozzarella"),
                ProductEntity(12, "BBQ Chicken Pizza", 700.0, "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=500", "Pizza", "BBQ sauce, chicken, red onion"),
                ProductEntity(13, "Four Cheese Pizza", 750.0, "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500", "Pizza", "Mozzarella, Cheddar, Parmesan, Blue cheese"),
                ProductEntity(14, "Seafood Pizza", 800.0, "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500", "Pizza", "Shrimp, mussels, and squid"),
                
                // 🥤 Drinks
                ProductEntity(15, "Coca-Cola 0.5L", 100.0, "https://images.unsplash.com/photo-1622483767028-3f66f32aef97?w=500", "Drinks", "Chilled 0.5L soda"),
                ProductEntity(16, "Fanta 0.5L", 100.0, "https://images.unsplash.com/photo-1624517452488-04869289c4ca?w=500", "Drinks", "Orange flavor 0.5L"),
                ProductEntity(17, "Sprite 0.5L", 100.0, "https://images.unsplash.com/photo-1625772290748-390b1df0ad41?w=500", "Drinks", "Lemon-lime flavor 0.5L"),
                ProductEntity(18, "Juice 1L", 180.0, "https://images.unsplash.com/photo-1613478223719-2ab802602423?w=500", "Drinks", "Assorted flavors 1L"),
                ProductEntity(19, "Water 0.5L", 60.0, "https://images.unsplash.com/photo-1523362628742-0c297f11ae55?w=500", "Drinks", "Pure still water 0.5L"),
                
                // 🍟 Snacks
                ProductEntity(20, "French Fries", 150.0, "https://images.unsplash.com/photo-1573080496219-bb080dd4f877?w=500", "Snacks", "Golden crispy fries"),
                ProductEntity(21, "Chicken Nuggets", 250.0, "https://images.unsplash.com/photo-1562967914-608f82629710?w=500", "Snacks", "6 pieces of tender chicken"),
                ProductEntity(22, "Onion Rings", 200.0, "https://images.unsplash.com/photo-1639024471283-035188835118?w=500", "Snacks", "Crispy onion rings")
            )
            sushiDao.insertProducts(products)
        }

        if (sushiDao.getRestaurantCount() == 0) {
            val restaurants = listOf(
                RestaurantEntity(1, "123 Manas Ave, Bishkek", "+996 555 123 456", "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=500"),
                RestaurantEntity(2, "45 Prospekt Mira, Bishkek", "+996 777 654 321", "https://images.unsplash.com/photo-1552566626-52f8b828add9?w=500")
            )
            sushiDao.insertRestaurants(restaurants)
        }
    }
}
