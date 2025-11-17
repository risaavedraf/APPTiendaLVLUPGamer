package com.example.tiendalvlupgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tiendalvlupgamer.data.repository.ImagenRepository
import com.example.tiendalvlupgamer.data.repository.ProfileRepository

class ProfileViewModelFactory(
    private val profileRepository: ProfileRepository,
    private val imagenRepository: ImagenRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(profileRepository, imagenRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
