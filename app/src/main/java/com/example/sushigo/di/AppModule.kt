package com.example.sushigo.di

import android.content.Context
import androidx.room.Room
import com.example.sushigo.data.local.SushiDatabase
import com.example.sushigo.data.local.dao.SushiDao
import com.example.sushigo.data.local.pref.UserPreferences
import com.example.sushigo.data.repository.SushiRepositoryImpl
import com.example.sushigo.domain.repository.SushiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SushiDatabase {
        return Room.databaseBuilder(
            context,
            SushiDatabase::class.java,
            "sushi_db"
        )
        .fallbackToDestructiveMigration() // Защищает от зависаний при обновлении БД
        .build()
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
}
