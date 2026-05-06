package com.example.sushigo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val cartId: Int = 0,
    val productId: Int,
    val productName: String,
    val price: Double,
    val image: String,
    val quantity: Int = 1
)
