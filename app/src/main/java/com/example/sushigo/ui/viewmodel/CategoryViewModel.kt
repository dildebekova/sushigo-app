package com.example.sushigo.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sushigo.domain.model.Product
import com.example.sushigo.domain.repository.SushiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val repository: SushiRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryName: String = checkNotNull(savedStateHandle["categoryName"])

    val products: StateFlow<List<Product>> = repository.getProductsByCategory(categoryName)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val title: String = categoryName
}
