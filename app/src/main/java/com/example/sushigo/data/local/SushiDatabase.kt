package com.example.sushigo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sushigo.data.local.dao.SushiDao
import com.example.sushigo.data.local.entity.CartEntity
import com.example.sushigo.data.local.entity.ProductEntity
import com.example.sushigo.data.local.entity.RestaurantEntity
import com.example.sushigo.data.local.entity.UserEntity

@Database(
    entities = [
        ProductEntity::class, 
        CartEntity::class, 
        RestaurantEntity::class, 
        UserEntity::class
    ],
    version = 2, // Увеличили версию с 1 до 2
    exportSchema = false
)
abstract class SushiDatabase : RoomDatabase() {
    abstract fun sushiDao(): SushiDao
}
