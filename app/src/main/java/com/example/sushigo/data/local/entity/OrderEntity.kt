package com.example.sushigo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userName: String,
    val itemsSummary: String,
    val totalPrice: Double,
    val timestamp: Long = System.currentTimeMillis()
)
