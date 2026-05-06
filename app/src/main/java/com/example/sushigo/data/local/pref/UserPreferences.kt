package com.example.sushigo.data.local.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferences @Inject constructor(private val context: Context) {
    companion object {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_PHONE = stringPreferencesKey("user_phone")
    }

    val isDarkMode: Flow<Boolean?> = context.dataStore.data.map { preferences ->
        preferences[IS_DARK_MODE]
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOGGED_IN] ?: false
    }

    val userData: Flow<Triple<String, String, Boolean>> = context.dataStore.data.map { preferences ->
        Triple(
            preferences[USER_NAME] ?: "",
            preferences[USER_PHONE] ?: "",
            preferences[IS_LOGGED_IN] ?: false
        )
    }

    suspend fun setDarkMode(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_DARK_MODE] = isDark
        }
    }

    suspend fun saveUser(name: String, phone: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = name
            preferences[USER_PHONE] = phone
            preferences[IS_LOGGED_IN] = true
        }
    }

    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = false
            preferences[USER_NAME] = ""
            preferences[USER_PHONE] = ""
        }
    }
}
