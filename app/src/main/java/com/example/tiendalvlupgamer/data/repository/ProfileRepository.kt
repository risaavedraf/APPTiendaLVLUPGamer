package com.example.tiendalvlupgamer.data.repository

import com.example.tiendalvlupgamer.data.network.ProfileApiService
import com.example.tiendalvlupgamer.model.FullProfileResponse
import com.example.tiendalvlupgamer.model.UpdateProfileRequest
import com.example.tiendalvlupgamer.model.UsuarioResponse
import retrofit2.Response

class ProfileRepository(private val profileApiService: ProfileApiService) {

    suspend fun getMyProfile(): Response<FullProfileResponse> {
        return profileApiService.getMyProfile()
    }

    suspend fun updateMyProfile(updateProfileRequest: UpdateProfileRequest): Response<UsuarioResponse> {
        return profileApiService.updateMyProfile(updateProfileRequest)
    }
}
