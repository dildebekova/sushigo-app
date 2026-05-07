package com.example.sushigo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sushigo.domain.model.CartItem

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val cartId: Int = 0,
    val productId: Int,
    val productName: String,
    val price: Double,
    val image: String,
    val quantity: Int = 1
)

fun CartEntity.toDomain() = CartItem(
    cartId = cartId,
    productId = productId,
    productName = productName,
    price = price,
    image = image,
    quantity = quantity
)

fun CartItem.toEntity() = CartEntity(
    cartId = cartId,
    productId = productId,
    productName = productName,
    price = price,
    image = image,
    quantity = quantity
)
