package com.example.tiendalvlupgamer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendalvlupgamer.data.repository.PedidoRepository
import com.example.tiendalvlupgamer.model.ApiErrorResponse
import com.example.tiendalvlupgamer.model.PedidoResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch

class PedidosViewModel(private val repository: PedidoRepository) : ViewModel() {

    private val _pedidos = MutableLiveData<List<PedidoResponse>>()
    val pedidos: LiveData<List<PedidoResponse>> = _pedidos

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadMisPedidos() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getMisPedidos()
                if (response.isSuccessful) {
                    _pedidos.postValue(response.body())
                } else {
                    val errorMessage = response.errorBody()?.string()?.let {
                        try {
                            Gson().fromJson(it, ApiErrorResponse::class.java).message
                        } catch (e: Exception) {
                            response.message()
                        }
                    } ?: response.message()
                    _error.postValue(errorMessage)
                }
            } catch (e: Exception) {
                _error.postValue("Excepción al cargar pedidos: ${e.message}")
            }
            _isLoading.value = false
        }
    }

    fun clearError() {
        _error.value = null
    }
}
