package com.example.tiendalvlupgamer.data.network

import com.example.tiendalvlupgamer.model.FullProfileResponse
import com.example.tiendalvlupgamer.model.UpdateProfileRequest
import com.example.tiendalvlupgamer.model.UsuarioResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ProfileApiService {

    @GET("/api/perfil/me")
    suspend fun getMyProfile(): Response<FullProfileResponse>

    @PUT("/api/perfil/me")
    suspend fun updateMyProfile(@Body updateProfileRequest: UpdateProfileRequest): Response<UsuarioResponse>
}
