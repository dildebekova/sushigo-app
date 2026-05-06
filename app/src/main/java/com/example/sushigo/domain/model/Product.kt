package com.example.sushigo.domain.model

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val image: String,
    val category: String,
    val description: String = ""
)
