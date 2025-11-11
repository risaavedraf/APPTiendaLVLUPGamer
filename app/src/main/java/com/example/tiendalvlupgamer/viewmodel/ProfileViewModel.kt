package com.example.tiendalvlupgamer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendalvlupgamer.data.repository.ProfileRepository
import com.example.tiendalvlupgamer.model.FullProfileResponse
import com.example.tiendalvlupgamer.model.UpdateProfileRequest
import com.example.tiendalvlupgamer.model.UsuarioResponse
import kotlinx.coroutines.launch

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

    private val _profile = MutableLiveData<FullProfileResponse?>()
    val profile: LiveData<FullProfileResponse?> = _profile

    private val _updateResult = MutableLiveData<UsuarioResponse?>()
    val updateResult: LiveData<UsuarioResponse?> = _updateResult

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun getMyProfile() {
        viewModelScope.launch {
            val response = profileRepository.getMyProfile()
            if (response.isSuccessful) {
                _profile.postValue(response.body())
            } else {
                _profile.postValue(null)
            }
        }
    }

    fun updateMyProfile(name: String, lastName: String, birthDate: String) {
        viewModelScope.launch {
            val request = UpdateProfileRequest(name, lastName, birthDate)
            val response = profileRepository.updateMyProfile(request)
            if (response.isSuccessful) {
                _updateResult.postValue(response.body())
            } else {
                _error.postValue("Error al actualizar. Verifique que los campos no estén vacíos.")
            }
        }
    }

    fun onUpdateHandled() {
        _updateResult.value = null
        _error.value = null
    }
}