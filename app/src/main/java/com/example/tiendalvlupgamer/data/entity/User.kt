package com.example.tiendalvlupgamer.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val lastName: String,
    val username: String,
    val birthDate: String,
    val email: String,
    val password: String
)