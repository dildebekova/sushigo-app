package com.example.sushigo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sushigo.data.local.entity.CartEntity
import com.example.sushigo.domain.repository.SushiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: SushiRepository
) : ViewModel() {

    val cartItems: StateFlow<List<CartEntity>> = repository.getCartItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalPrice: StateFlow<Double> = cartItems.map { items ->
        items.sumOf { it.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun updateQuantity(item: CartEntity, delta: Int) {
        viewModelScope.launch {
            repository.updateCartItem(item.copy(quantity = item.quantity + delta))
        }
    }

    fun removeFromCart(item: CartEntity) {
        viewModelScope.launch {
            repository.removeFromCart(item)
        }
    }
}
