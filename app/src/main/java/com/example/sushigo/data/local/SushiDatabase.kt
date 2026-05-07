package com.example.sushigo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sushigo.data.local.dao.SushiDao
import com.example.sushigo.data.local.entity.*

@Database(
    entities = [
        ProductEntity::class, 
        CartEntity::class, 
        RestaurantEntity::class, 
        UserEntity::class,
        OrderEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class SushiDatabase : RoomDatabase() {
    abstract fun sushiDao(): SushiDao
}
