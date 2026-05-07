package com.example.sushigo.domain.model

data class Order(
    val id: Int = 0,
    val userName: String,
    val itemsSummary: String,
    val totalPrice: Double,
    val timestamp: Long
)
