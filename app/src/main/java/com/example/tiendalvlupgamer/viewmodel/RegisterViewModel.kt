package com.example.tiendalvlupgamer.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendalvlupgamer.data.dao.UserDao
import com.example.tiendalvlupgamer.data.repository.AuthRepository
import com.example.tiendalvlupgamer.model.RegistroRequest
import com.example.tiendalvlupgamer.util.ValidationHelper
import com.example.tiendalvlupgamer.model.RegisterUiState
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class RegisterViewModel(private val userDao: UserDao, private val authRepository: AuthRepository) : ViewModel() {

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

    private fun validate(): String? {
        val nameErr = ValidationHelper.validateRequired(uiState.name, "Nombre")
        val lastNameErr = ValidationHelper.validateRequired(uiState.lastName, "Apellido")
        val usernameErr = ValidationHelper.validateUsername(uiState.username)
        val emailErr = ValidationHelper.validateEmail(uiState.email)
        val passwordErr = ValidationHelper.validatePassword(uiState.password)
        val birthDateErr = ValidationHelper.validateDate(uiState.birthDate, "dd/MM/yyyy")

        uiState = uiState.copy(
            nameError = nameErr,
            lastNameError = lastNameErr,
            usernameError = usernameErr,
            emailError = emailErr,
            passwordError = passwordErr,
            birthDateError = birthDateErr
        )

        return nameErr ?: lastNameErr ?: usernameErr ?: emailErr ?: passwordErr ?: birthDateErr
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
                val formattedBirthDate = try {
                    val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val date = inputFormat.parse(uiState.birthDate)
                    outputFormat.format(date!!)
                } catch (e: Exception) {
                    uiState = uiState.copy(
                        loading = false,
                        error = "Formato de fecha inválido. Use dd/MM/yyyy"
                    )
                    return@launch
                }

                val request = RegistroRequest(
                    name = uiState.name,
                    lastName = uiState.lastName,
                    username = uiState.username,
                    birthDate = formattedBirthDate,
                    email = uiState.email,
                    password = uiState.password
                )

                authRepository.register(request)

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