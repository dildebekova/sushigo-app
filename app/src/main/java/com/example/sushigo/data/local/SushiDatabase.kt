package com.example.sushigo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sushigo.data.local.dao.SushiDao
import com.example.sushigo.data.local.entity.CartEntity
import com.example.sushigo.data.local.entity.ProductEntity
import com.example.sushigo.data.local.entity.RestaurantEntity

@Database(
    entities = [ProductEntity::class, CartEntity::class, RestaurantEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SushiDatabase : RoomDatabase() {
    abstract fun sushiDao(): SushiDao
}
