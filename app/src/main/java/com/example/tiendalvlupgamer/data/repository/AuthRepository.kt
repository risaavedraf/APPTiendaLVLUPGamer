package com.example.tiendalvlupgamer.data.repository

import com.example.tiendalvlupgamer.data.network.AuthApiService
import com.example.tiendalvlupgamer.data.network.RetrofitClient
import com.example.tiendalvlupgamer.model.LoginRequest
import com.example.tiendalvlupgamer.model.RegistroRequest

class AuthRepository(private val apiService: AuthApiService) {

    suspend fun login(email: String, password: String) = apiService.login(LoginRequest(email, password))

    suspend fun register(request: RegistroRequest) = apiService.register(request)
}
