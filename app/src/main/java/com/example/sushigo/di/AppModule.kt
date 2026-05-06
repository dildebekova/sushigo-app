package com.example.sushigo.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.sushigo.data.local.SushiDatabase
import com.example.sushigo.data.local.dao.SushiDao
import com.example.sushigo.data.local.entity.ProductEntity
import com.example.sushigo.data.local.entity.RestaurantEntity
import com.example.sushigo.data.local.pref.UserPreferences
import com.example.sushigo.data.repository.SushiRepositoryImpl
import com.example.sushigo.domain.repository.SushiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        @ApplicationScope scope: CoroutineScope,
        daoProvider: Provider<SushiDao>
    ): SushiDatabase {
        return Room.databaseBuilder(
            context,
            SushiDatabase::class.java,
            "sushi_db"
        )
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                scope.launch {
                    seedDatabase(daoProvider.get())
                }
            }
        })
        .fallbackToDestructiveMigration()
        .build()
    }

    private suspend fun seedDatabase(dao: SushiDao) {
        val products = listOf(
            ProductEntity(1, "Сет 'Самурай'", 1200.0, "https://thumb.tildacdn.com/tild3131-3165-4537-b933-653131333334/-/resize/500x/-/format/webp/1.jpg", "Сеты", "Огромный сет для большой компании."),
            ProductEntity(2, "Филадельфия", 450.0, "https://thumb.tildacdn.com/tild3563-3331-4131-b035-373866386431/-/resize/500x/-/format/webp/___2.jpg", "Сеты", "Классическая филадельфия с лососем."),
            ProductEntity(3, "Комбо №1", 800.0, "https://thumb.tildacdn.com/tild3838-3434-4531-b032-613463376231/-/resize/500x/-/format/webp/1.jpg", "Комбо наборы", "Роллы + напиток."),
            ProductEntity(4, "Кока-кола", 150.0, "https://thumb.tildacdn.com/tild3766-3030-4139-b932-353239333961/-/resize/500x/-/format/webp/___1.jpg", "Напитки", "0.5л охлажденная."),
            ProductEntity(5, "Пицца Маргарита", 600.0, "https://thumb.tildacdn.com/tild6535-3161-4638-b738-313636306566/-/resize/500x/-/format/webp/___1.jpg", "Пицца", "Традиционная итальянская пицца."),
            ProductEntity(6, "Чизкейк", 250.0, "https://thumb.tildacdn.com/tild6130-3765-4131-b131-643362306231/-/resize/500x/-/format/webp/___1.jpg", "Сладости", "Нежный классический десерт.")
        )
        dao.insertProducts(products)

        val restaurants = listOf(
            RestaurantEntity(1, "ул. Ленина, 10", "+7 (999) 123-45-67", "https://thumb.tildacdn.com/tild3233-3132-4138-b431-363435643431/-/resize/500x/-/format/webp/1.jpg"),
            RestaurantEntity(2, "пр. Мира, 45", "+7 (999) 765-43-21", "https://thumb.tildacdn.com/tild6338-6638-4663-a238-646633633731/-/resize/500x/-/format/webp/2.jpg")
        )
        dao.insertRestaurants(restaurants)
    }

    @Provides
    fun provideSushiDao(database: SushiDatabase): SushiDao {
        return database.sushiDao()
    }

    @Provides
    @Singleton
    fun provideSushiRepository(sushiDao: SushiDao): SushiRepository {
        return SushiRepositoryImpl(sushiDao)
    }

    @Provides
    @Singleton
    fun provideUserPreferences(@ApplicationContext context: Context): UserPreferences {
        return UserPreferences(context)
    }

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
}
