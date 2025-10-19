package com.example.tiendalvlupgamer.model

import android.net.Uri

data class RegisterUiState(
    val name: String = "",
    val lastName: String = "",
    val username: String = "",
    val birthDate: String = "",
    val email: String = "",
    val password: String = "",
    val profileImageUri: Uri? = null,
    val loading: Boolean = false,
    val error: String? = null,
    val nameError: String? = null,
    val lastNameError: String? = null,
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val registrationSuccess: Boolean = false
)
