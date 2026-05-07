package com.example.sushigo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sushigo.data.local.pref.UserPreferences
import com.example.sushigo.domain.model.CartItem
import com.example.sushigo.domain.model.Order
import com.example.sushigo.domain.repository.SushiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: SushiRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    val cartItems: StateFlow<List<CartItem>> = repository.getCartItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalPrice: StateFlow<Double> = cartItems.map { items ->
        items.sumOf { it.totalLinePrice }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    private val _checkoutEvent = MutableSharedFlow<CheckoutResult>()
    val checkoutEvent = _checkoutEvent.asSharedFlow()

    fun updateQuantity(item: CartItem, delta: Int) {
        viewModelScope.launch {
            repository.updateCartItem(item.copy(quantity = item.quantity + delta))
        }
    }

    fun removeFromCart(item: CartItem) {
        viewModelScope.launch {
            repository.removeFromCart(item)
        }
    }

    fun checkout() {
        viewModelScope.launch {
            val session = userPreferences.userData.first()
            val userName = session.user.name
            val isLoggedIn = session.isLoggedIn

            if (!isLoggedIn || userName.isBlank()) {
                _checkoutEvent.emit(CheckoutResult.Error("To place an order, please register in settings"))
                return@launch
            }

            val items = cartItems.value
            if (items.isEmpty()) return@launch

            val summary = items.joinToString(", ") { "${it.productName} x${it.quantity}" }
            val order = Order(
                userName = userName,
                itemsSummary = summary,
                totalPrice = totalPrice.value,
                timestamp = System.currentTimeMillis()
            )

            repository.placeOrder(order)
            repository.clearCart()
            _checkoutEvent.emit(CheckoutResult.Success("Order placed successfully!"))
        }
    }

    sealed class CheckoutResult {
        data class Success(val message: String) : CheckoutResult()
        data class Error(val message: String) : CheckoutResult()
    }
}
