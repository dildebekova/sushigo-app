package com.example.sushigo.domain.model

data class CartItem(
    val cartId: Int = 0,
    val userName: String = "",
    val productId: Int,
    val productName: String,
    val price: Double,
    val image: String,
    val quantity: Int
) {
    val totalLinePrice: Double get() = price * quantity
}
