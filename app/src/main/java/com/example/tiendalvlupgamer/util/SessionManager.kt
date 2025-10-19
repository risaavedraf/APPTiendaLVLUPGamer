package com.example.tiendalvlupgamer.util

import com.example.tiendalvlupgamer.data.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Un objeto Singleton para gestionar la sesión del usuario en toda la aplicación.
 */
object SessionManager {

    // _currentUser es privado para que solo el SessionManager pueda modificarlo.
    private val _currentUser = MutableStateFlow<User?>(null)

    // currentUser es público y de solo lectura (StateFlow) para que la UI pueda observarlo.
    val currentUser: StateFlow<User?> = _currentUser

    /**
     * Inicia una nueva sesión con el usuario proporcionado.
     * @param user El usuario que ha iniciado sesión.
     */
    fun login(user: User) {
        _currentUser.value = user
    }

    /**
     * Cierra la sesión actual, estableciendo el usuario a null.
     */
    fun logout() {
        _currentUser.value = null
    }
}
