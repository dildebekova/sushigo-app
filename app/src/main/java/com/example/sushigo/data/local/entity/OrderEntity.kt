package com.example.sushigo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sushigo.domain.model.Order

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userName: String,
    val itemsSummary: String,
    val totalPrice: Double,
    val timestamp: Long = System.currentTimeMillis()
)

fun OrderEntity.toDomain() = Order(
    id = id,
    userName = userName,
    itemsSummary = itemsSummary,
    totalPrice = totalPrice,
    timestamp = timestamp
)

fun Order.toEntity() = OrderEntity(
    id = id,
    userName = userName,
    itemsSummary = itemsSummary,
    totalPrice = totalPrice,
    timestamp = timestamp
)
