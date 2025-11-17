package com.example.tiendalvlupgamer.util

import com.example.tiendalvlupgamer.model.UsuarioResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SessionManager {

    private val _currentUser = MutableStateFlow<UsuarioResponse?>(null)
    val currentUser: StateFlow<UsuarioResponse?> = _currentUser

    private var _token: String? = null

    fun login(user: UsuarioResponse, token: String) {
        _currentUser.value = user
        _token = token
    }

    fun logout() {
        _currentUser.value = null
        _token = null
    }

    fun getToken(): String? {
        return _token
    }
}
