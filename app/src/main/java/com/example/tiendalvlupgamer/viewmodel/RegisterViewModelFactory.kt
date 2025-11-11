package com.example.tiendalvlupgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tiendalvlupgamer.data.dao.UserDao
import com.example.tiendalvlupgamer.data.network.RetrofitClient
import com.example.tiendalvlupgamer.data.repository.AuthRepository

class RegisterViewModelFactory(private val userDao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            val authRepository = AuthRepository(RetrofitClient.authApiService)
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(userDao, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}