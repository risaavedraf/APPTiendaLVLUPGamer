package com.example.tiendalvlupgamer.model

import android.net.Uri

data class RegisterUiState(
    val name: String = "",
    val nameError: String? = null,
    val lastName: String = "",
    val lastNameError: String? = null,
    val username: String = "",
    val usernameError: String? = null,
    val birthDate: String = "",
    val birthDateError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val profileImageUri: Uri? = null,
    val loading: Boolean = false,
    val registrationSuccess: Boolean = false,
    val error: String? = null
)
