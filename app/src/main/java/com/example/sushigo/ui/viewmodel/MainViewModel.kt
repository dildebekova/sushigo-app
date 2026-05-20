package com.example.sushigo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sushigo.data.local.pref.UserPreferences
import com.example.sushigo.domain.model.User
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
        UserPreferences.UserSession(User("", ""), false)
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

    fun register(name: String, phone: String, password: String) {
        if (name.isBlank() || phone.isBlank() || password.isBlank()) {
            _authError.value = "Please fill in all fields"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.registerUser(name, phone, password)
                userPreferences.saveUser(name, phone)
                _authError.value = null
            } catch (e: Exception) {
                _authError.value = "Registration error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login(name: String, password: String) {
        if (name.isBlank() || password.isBlank()) {
            _authError.value = "Please enter name and password"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val user = repository.loginUser(name, password)
                if (user != null) {
                    userPreferences.saveUser(user.name, user.phone)
                    _authError.value = null
                } else {
                    _authError.value = "Invalid name or password"
                }
            } catch (e: Exception) {
                _authError.value = "Login error: ${e.message}"
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
