package com.example.sushigo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sushigo.data.local.pref.UserPreferences
import com.example.sushigo.domain.model.Order
import com.example.sushigo.domain.repository.SushiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class NotificationsViewModel @Inject constructor(
    private val repository: SushiRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val orders: StateFlow<List<Order>> = userPreferences.userData
        .flatMapLatest { session ->
            val name = session.user.name
            val isLoggedIn = session.isLoggedIn
            if (isLoggedIn && name.isNotBlank()) {
                repository.getOrdersByUserName(name)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val isLoggedIn: StateFlow<Boolean> = userPreferences.isLoggedIn
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
}
