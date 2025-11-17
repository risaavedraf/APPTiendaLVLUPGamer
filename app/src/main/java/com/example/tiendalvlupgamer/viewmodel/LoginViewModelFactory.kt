package com.example.tiendalvlupgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tiendalvlupgamer.data.dao.UserDao
import com.example.tiendalvlupgamer.data.network.RetrofitClient
import com.example.tiendalvlupgamer.data.repository.AuthRepository

class LoginViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            val authRepository = AuthRepository(RetrofitClient.authApiService)
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userDao, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
