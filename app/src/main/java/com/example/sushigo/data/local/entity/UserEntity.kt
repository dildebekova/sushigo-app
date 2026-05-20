package com.example.sushigo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sushigo.domain.model.User

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val name: String,
    val phone: String,
    val password: String
)

fun UserEntity.toDomain() = User(name, phone, password)
fun User.toEntity() = UserEntity(name, phone, password)
