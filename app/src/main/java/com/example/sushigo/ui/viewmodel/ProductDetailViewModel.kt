package com.example.sushigo.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sushigo.domain.model.Product
import com.example.sushigo.domain.repository.SushiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: SushiRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: Int = checkNotNull(savedStateHandle["productId"])
    
    private val _product = MutableStateFlow<Product?>(null)
    val product = _product.asStateFlow()

    init {
        viewModelScope.launch {
            _product.value = repository.getProductById(productId)
        }
    }

    fun addToCart() {
        product.value?.let {
            viewModelScope.launch {
                repository.addToCart(it)
            }
        }
    }
}
