package com.example.sushigo.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String = "", val icon: ImageVector? = null) {
    object Home : Screen("home", "Меню", Icons.Default.Home)
    object Restaurants : Screen("restaurants", "Заведения", Icons.Default.LocationOn)
    object Cart : Screen("cart", "Корзина", Icons.Default.ShoppingCart)
    object Settings : Screen("settings", "Профиль", Icons.Default.Person)
    
    object CategoryProducts : Screen("category/{categoryName}") {
        fun createRoute(categoryName: String) = "category/$categoryName"
    }
    object ProductDetail : Screen("product/{productId}") {
        fun createRoute(productId: Int) = "product/$productId"
    }
    object RestaurantDetail : Screen("restaurant/{restaurantId}") {
        fun createRoute(restaurantId: Int) = "restaurant/$restaurantId"
    }
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Restaurants,
    Screen.Cart,
    Screen.Settings
)
