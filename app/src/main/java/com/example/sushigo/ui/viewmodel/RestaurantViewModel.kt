package com.example.sushigo.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sushigo.domain.model.Restaurant
import com.example.sushigo.domain.repository.SushiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class RestaurantViewModel @Inject constructor(
    private val repository: SushiRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val restaurants: StateFlow<List<Restaurant>> = repository.getRestaurants()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val restaurantId: Int? = savedStateHandle.get<Int>("restaurantId")
    
    val selectedRestaurant: StateFlow<Restaurant?> = restaurants.map { list ->
        list.find { it.id == restaurantId }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )
}
