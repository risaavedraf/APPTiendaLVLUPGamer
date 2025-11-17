package com.example.tiendalvlupgamer.util

import java.text.SimpleDateFormat
import java.util.Locale

object ValidationHelper {

    fun validateRequired(value: String, fieldName: String): String? {
        return if (value.isEmpty()) "El campo $fieldName es requerido" else null
    }

    fun validateEmail(email: String): String? {
        if (email.isEmpty()) return "El correo es requerido"
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) return "Formato de correo inválido"
        return null
    }

    fun validatePassword(password: String): String? {
        if (password.isEmpty()) return "La contraseña es requerida"
        if (password.length < 6) return "La contraseña debe tener al menos 6 caracteres"
        return null
    }

    fun validateUsername(username: String): String? {
        if (username.isEmpty()) return "El username es requerido"
        if (username.length < 4) return "El username debe tener al menos 4 caracteres"
        return null
    }

    fun validateDate(date: String, format: String): String? {
        if (date.isEmpty()) return "La fecha de nacimiento es requerida"
        return try {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            sdf.isLenient = false
            sdf.parse(date)
            null
        } catch (e: Exception) {
            "Formato de fecha inválido. Use $format"
        }
    }
}