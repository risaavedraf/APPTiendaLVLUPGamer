package com.example.tiendalvlupgamer.data.network

import com.example.tiendalvlupgamer.model.LoginRequest
import com.example.tiendalvlupgamer.model.LoginResponse
import com.example.tiendalvlupgamer.model.RegistroRequest
import com.example.tiendalvlupgamer.model.UsuarioResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/auth/register")
    suspend fun register(@Body request: RegistroRequest): UsuarioResponse

    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
