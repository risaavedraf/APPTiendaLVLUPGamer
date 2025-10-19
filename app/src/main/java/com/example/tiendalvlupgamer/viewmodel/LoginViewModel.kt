package com.example.tiendalvlupgamer.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendalvlupgamer.data.database.AppDatabase
import com.example.tiendalvlupgamer.model.LoginUiState
import com.example.tiendalvlupgamer.util.ValidationHelper
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.getDatabase(application).userDao()

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailOrUsernameChange(v: String) {
        uiState = uiState.copy(
            emailOrUsername = v,
            emailOrUsernameError = if (v.isEmpty()) "El correo o username es requerido" else null,
            error = null
        )
    }

    fun onPasswordChange(v: String) {
        uiState = uiState.copy(
            password = v,
            passwordError = if (v.isEmpty()) "La contraseña es requerida" else null,
            error = null
        )
    }

    private fun validate(): String? {
        val emailOrUsernameErr = ValidationHelper.validateRequired(uiState.emailOrUsername, "Correo o username")
        val passwordErr = ValidationHelper.validateRequired(uiState.password, "Contraseña")

        uiState = uiState.copy(
            emailOrUsernameError = emailOrUsernameErr,
            passwordError = passwordErr
        )

        return emailOrUsernameErr ?: passwordErr
    }

    fun tryLogin(onSuccess: () -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(loading = true, error = null)

            val err = validate()
            if (err != null) {
                uiState = uiState.copy(error = err, loading = false)
                return@launch
            }

            try {
                // Buscar usuario por email o username
                val user = userDao.login(uiState.emailOrUsername, uiState.password)

                if (user != null) {
                    // Login exitoso
                    uiState = uiState.copy(
                        loading = false,
                        loginSuccess = true,
                        error = null
                    )
                    onSuccess()
                } else {
                    // Credenciales incorrectas
                    uiState = uiState.copy(
                        loading = false,
                        error = "Usuario o contraseña incorrectos"
                    )
                }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    loading = false,
                    error = "Error al iniciar sesión: ${e.message}"
                )
            }
        }
    }
}
