package com.example.sushigo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sushigo.data.local.pref.UserPreferences
import com.example.sushigo.domain.repository.SushiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val repository: SushiRepository
) : ViewModel() {

    val isDarkMode = userPreferences.isDarkMode.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    val userData = userPreferences.userData.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        Triple("", "", false)
    )

    private val _authError = MutableStateFlow<String?>(null)
    val authError = _authError.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            userPreferences.setDarkMode(isDark)
        }
    }

    fun register(name: String, phone: String) {
        if (name.isBlank() || phone.isBlank()) {
            _authError.value = "Заполните все поля"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                println("AuthDebug: Registering user $name")
                repository.registerUser(name, phone)
                userPreferences.saveUser(name, phone)
                _authError.value = null
            } catch (e: Exception) {
                _authError.value = "Ошибка базы данных: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login(name: String) {
        if (name.isBlank()) {
            _authError.value = "Введите имя"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                println("AuthDebug: Trying to login user $name")
                val user = repository.loginUser(name)
                if (user != null) {
                    println("AuthDebug: User found, saving to preferences")
                    userPreferences.saveUser(user.name, user.phone)
                    _authError.value = null
                } else {
                    println("AuthDebug: User not found in DB")
                    _authError.value = "Пользователь не найден"
                }
            } catch (e: Exception) {
                _authError.value = "Ошибка: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.logout()
        }
    }

    fun clearError() {
        _authError.value = null
    }
}
