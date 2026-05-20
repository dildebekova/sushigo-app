package com.example.sushigo.domain.model

data class User(
    val name: String,
    val phone: String,
    val password: String = ""
)
