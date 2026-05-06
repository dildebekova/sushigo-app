package com.example.sushigo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sushigo.domain.model.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val price: Double,
    val image: String,
    val category: String,
    val description: String
)

fun ProductEntity.toDomain() = Product(id, name, price, image, category, description)
fun Product.toEntity() = ProductEntity(id, name, price, image, category, description)
