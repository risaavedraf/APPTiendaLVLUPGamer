package com.example.tiendalvlupgamer.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendalvlupgamer.data.dao.UserDao
import com.example.tiendalvlupgamer.data.entity.User
import com.example.tiendalvlupgamer.util.ValidationHelper
import com.example.tiendalvlupgamer.model.RegisterUiState
import kotlinx.coroutines.launch

class RegisterViewModel(private val userDao: UserDao) : ViewModel() {

    var uiState by mutableStateOf(RegisterUiState())
        private set

    fun onNameChange(v: String) {
        uiState = uiState.copy(
            name = v,
            nameError = if (v.isEmpty()) "El nombre es requerido" else null,
            error = null
        )
    }

    fun onLastNameChange(v: String) {
        uiState = uiState.copy(
            lastName = v,
            lastNameError = if (v.isEmpty()) "El apellido es requerido" else null,
            error = null
        )
    }

    fun onUsernameChange(v: String) {
        uiState = uiState.copy(
            username = v,
            usernameError = if (v.isEmpty()) "El username es requerido" else ValidationHelper.validateUsername(v),
            error = null
        )
    }

    fun onBirthDateChange(v: String) {
        uiState = uiState.copy(birthDate = v, error = null)
    }

    fun onEmailChange(v: String) {
        uiState = uiState.copy(
            email = v,
            emailError = ValidationHelper.validateEmail(v),
            error = null
        )
    }

    fun onPasswordChange(v: String) {
        uiState = uiState.copy(
            password = v,
            passwordError = ValidationHelper.validatePassword(v),
            error = null
        )
    }

    fun onProfileImageChange(uri: Uri?) {
        uiState = uiState.copy(profileImageUri = uri)
    }

    fun setLoading(loading: Boolean) {
        uiState = uiState.copy(loading = loading)
    }

    private suspend fun validate(): String? {
        val nameErr = ValidationHelper.validateRequired(uiState.name, "Nombre")
        val lastNameErr = ValidationHelper.validateRequired(uiState.lastName, "Apellido")
        val usernameErr = ValidationHelper.validateUsername(uiState.username)
        val emailErr = ValidationHelper.validateEmail(uiState.email)
        val passwordErr = ValidationHelper.validatePassword(uiState.password)

        val existingEmail = userDao.getUserByEmail(uiState.email)
        val emailExistsErr = if (existingEmail != null) "Este correo ya está registrado" else null

        val existingUsername = userDao.getUserByUsername(uiState.username)
        val usernameExistsErr = if (existingUsername != null) "Este username ya está en uso" else null

        uiState = uiState.copy(
            nameError = nameErr,
            lastNameError = lastNameErr,
            usernameError = usernameErr ?: usernameExistsErr,
            emailError = emailErr ?: emailExistsErr,
            passwordError = passwordErr
        )

        return nameErr ?: lastNameErr ?: usernameErr ?: usernameExistsErr ?: emailErr ?: emailExistsErr ?: passwordErr
    }

    fun tryRegister(onSuccess: () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true)

            val err = validate()
            if (err != null) {
                uiState = uiState.copy(error = err, loading = false)
                return@launch
            }

            try {
                val user = User(
                    name = uiState.name,
                    lastName = uiState.lastName,
                    username = uiState.username,
                    birthDate = uiState.birthDate,
                    email = uiState.email,
                    password = uiState.password
                )

                userDao.insertUser(user)

                uiState = uiState.copy(
                    loading = false,
                    registrationSuccess = true,
                    error = null
                )

                onSuccess()
            } catch (e: Exception) {
                uiState = uiState.copy(
                    loading = false,
                    error = "Error al registrar: ${e.message}"
                )
            }
        }
    }
}