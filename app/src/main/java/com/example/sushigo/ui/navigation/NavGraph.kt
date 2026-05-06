package com.example.sushigo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sushigo.ui.screens.*

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onCategoryClick = { category ->
                    navController.navigate(Screen.CategoryProducts.createRoute(category))
                }
            )
        }

        composable(
            route = Screen.CategoryProducts.route,
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) {
            CategoryScreen(
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) {
            ProductDetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Restaurants.route) {
            RestaurantsScreen(
                onRestaurantClick = { id ->
                    navController.navigate(Screen.RestaurantDetail.createRoute(id))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.RestaurantDetail.route,
            arguments = listOf(navArgument("restaurantId") { type = NavType.IntType })
        ) {
            RestaurantDetailScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}
