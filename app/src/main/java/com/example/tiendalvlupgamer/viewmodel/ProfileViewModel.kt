package com.example.tiendalvlupgamer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendalvlupgamer.data.repository.ImagenRepository
import com.example.tiendalvlupgamer.data.repository.ProfileRepository
import com.example.tiendalvlupgamer.model.FullProfileResponse
import com.example.tiendalvlupgamer.model.UpdateProfileRequest
import com.example.tiendalvlupgamer.model.UsuarioResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val imagenRepository: ImagenRepository
    ) : ViewModel() {

    private val _profile = MutableLiveData<FullProfileResponse?>()
    val profile: LiveData<FullProfileResponse?> = _profile

    private val _updateResult = MutableLiveData<UsuarioResponse?>()
    val updateResult: LiveData<UsuarioResponse?> = _updateResult

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _imageUploadResult = MutableLiveData<String?>()
    val imageUploadResult: LiveData<String?> = _imageUploadResult

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

    fun uploadProfileImage(userId: Long, imagePart: MultipartBody.Part) {
        viewModelScope.launch {
            try {
                val response = imagenRepository.subirImagenUsuario(userId, imagePart)
                if (response.isSuccessful) {
                    _imageUploadResult.postValue("Imagen subida con éxito")
                    delay(1000)
                    getMyProfile()
                } else {
                    _imageUploadResult.postValue("Error al subir la imagen: ${response.message()}")
                }
            } catch (e: Exception) {
                _imageUploadResult.postValue("Excepción al subir la imagen: ${e.message}")
            }
        }
    }

    fun onUpdateHandled() {
        _updateResult.value = null
        _error.value = null
    }

    fun onImageUploadHandled() {
        _imageUploadResult.value = null
    }
}