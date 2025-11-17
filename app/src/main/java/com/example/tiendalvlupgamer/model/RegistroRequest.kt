package com.example.tiendalvlupgamer.model

data class RegistroRequest(
    val username: String,
    val email: String,
    val password: String,
    val name: String,
    val lastName: String,
    val birthDate: String
)
