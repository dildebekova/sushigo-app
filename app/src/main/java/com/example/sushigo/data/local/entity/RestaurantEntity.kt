package com.example.sushigo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sushigo.domain.model.Restaurant

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey val id: Int,
    val address: String,
    val phone: String,
    val image: String
)

fun RestaurantEntity.toDomain() = Restaurant(id, address, phone, image)
fun Restaurant.toEntity() = RestaurantEntity(id, address, phone, image)
