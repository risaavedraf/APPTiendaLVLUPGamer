package com.example.tiendalvlupgamer.model

data class LoginUiState(
    val emailOrUsername: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val emailOrUsernameError: String? = null,
    val passwordError: String? = null,
    val loginSuccess: Boolean = false
)
