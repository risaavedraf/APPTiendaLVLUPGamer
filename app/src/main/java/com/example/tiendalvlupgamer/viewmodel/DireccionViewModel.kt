package com.example.tiendalvlupgamer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendalvlupgamer.data.repository.DireccionRepository
import com.example.tiendalvlupgamer.model.DireccionRequest
import com.example.tiendalvlupgamer.model.DireccionResponse
import kotlinx.coroutines.launch

class DireccionViewModel(private val repository: DireccionRepository) : ViewModel() {

    private val _direcciones = MutableLiveData<List<DireccionResponse>>()
    val direcciones: LiveData<List<DireccionResponse>> = _direcciones

    private val _direccion = MutableLiveData<DireccionResponse?>()
    val direccion: LiveData<DireccionResponse?> = _direccion

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _operationSuccess = MutableLiveData<Boolean>()
    val operationSuccess: LiveData<Boolean> = _operationSuccess

    fun loadDirecciones() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getMisDirecciones()
                if (response.isSuccessful) {
                    _direcciones.postValue(response.body())
                } else {
                    _error.postValue("Error al cargar las direcciones: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Excepción al cargar direcciones: ${e.message}")
            }
            _isLoading.value = false
        }
    }

    fun loadDireccionById(id: Long) {
        if (id == 0L) {
            _direccion.postValue(null)
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getDireccionById(id)
                if (response.isSuccessful) {
                    _direccion.postValue(response.body())
                } else {
                    _error.postValue("Error al cargar la dirección: ${response.message()}")
                }
            } catch (e: Exception) {
                _error.postValue("Excepción al cargar dirección: ${e.message}")
            }
            _isLoading.value = false
        }
    }

    fun createDireccion(request: DireccionRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.createDireccion(request)
                if (response.isSuccessful) {
                    _operationSuccess.postValue(true)
                } else {
                    _error.postValue("Error al crear la dirección: ${response.message()}")
                    _operationSuccess.postValue(false)
                }
            } catch (e: Exception) {
                _error.postValue("Excepción al crear dirección: ${e.message}")
                _operationSuccess.postValue(false)
            }
            _isLoading.value = false
        }
    }

    fun updateDireccion(id: Long, request: DireccionRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.updateDireccion(id, request)
                if (response.isSuccessful) {
                    _operationSuccess.postValue(true)
                } else {
                    _error.postValue("Error al actualizar la dirección: ${response.message()}")
                    _operationSuccess.postValue(false)
                }
            } catch (e: Exception) {
                _error.postValue("Excepción al actualizar dirección: ${e.message}")
                _operationSuccess.postValue(false)
            }
            _isLoading.value = false
        }
    }

    fun deleteDireccion(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.deleteDireccion(id)
                if (response.isSuccessful) {
                    _operationSuccess.postValue(true)
                } else {
                    _error.postValue("Error al eliminar la dirección: ${response.message()}")
                    _operationSuccess.postValue(false)
                }
            } catch (e: Exception) {
                _error.postValue("Excepción al eliminar dirección: ${e.message}")
                _operationSuccess.postValue(false)
            }
            _isLoading.value = false
        }
    }
    
    fun onOperationConsumed() {
        _operationSuccess.value = false
        _error.value = null
    }
}
