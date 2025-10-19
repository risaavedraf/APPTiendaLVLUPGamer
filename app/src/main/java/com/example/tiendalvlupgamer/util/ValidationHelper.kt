package com.example.tiendalvlupgamer.util

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object ValidationHelper {

    fun validateEmail(email: String): String? {
        if (email.isBlank()) return "El correo es obligatorio"
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        if (!emailRegex.matches(email)) {
            return "Formato de correo inválido (ej: correo@dominio.cl)"
        }
        return null
    }

    fun validatePassword(password: String): String? {
        if (password.isBlank()) return "La contraseña es obligatoria"
        if (password.length < 8) return "La contraseña debe tener al menos 8 caracteres"
        if (!password.any { it.isUpperCase() }) return "Debe contener al menos 1 mayúscula"
        if (!password.any { it.isLowerCase() }) return "Debe contener al menos 1 minúscula"
        if (!password.any { it.isDigit() }) return "Debe contener al menos 1 número"
        if (!password.any { !it.isLetterOrDigit() }) return "Debe contener al menos 1 símbolo"
        return null
    }

    fun validateUsername(username: String): String? {
        if (username.isBlank()) return "El nombre de usuario es obligatorio"
        if (username.length < 3) return "Debe tener al menos 3 caracteres"
        if (!username.matches("^[a-zA-Z0-9_]+$".toRegex())) {
            return "Solo puede contener letras, números y guión bajo"
        }
        return null
    }

    fun validateRequired(value: String, fieldName: String): String? {
        return if (value.isBlank()) "$fieldName es obligatorio" else null
    }


}
