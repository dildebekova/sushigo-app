package com.example.sushigo.data.remote

import com.example.sushigo.domain.model.Product
import com.example.sushigo.domain.model.Restaurant
import retrofit2.http.GET

interface SushiApi {
    @GET("products")
    suspend fun getProducts(): List<Product>

    @GET("restaurants")
    suspend fun getRestaurants(): List<Restaurant>
}
