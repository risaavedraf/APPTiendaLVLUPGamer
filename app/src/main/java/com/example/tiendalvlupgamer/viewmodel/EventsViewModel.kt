package com.example.tiendalvlupgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendalvlupgamer.data.repository.EventoRepository
import com.example.tiendalvlupgamer.model.EventoResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventsViewModel(private val repository: EventoRepository) : ViewModel() {

    private val _events = MutableStateFlow<List<EventoResponse>>(emptyList())
    val events: StateFlow<List<EventoResponse>> = _events

    private val _selectedEvent = MutableStateFlow<EventoResponse?>(null)
    val selectedEvent: StateFlow<EventoResponse?> = _selectedEvent

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadEvents()
    }

    fun loadEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getEventos()
                if (response.isSuccessful) {
                    _events.value = response.body()?.content ?: emptyList()
                } else {
                    _error.value = "Error al cargar los eventos: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Excepción al cargar eventos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getEventById(id: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.getEventoById(id)
                if (response.isSuccessful) {
                    _selectedEvent.value = response.body()
                } else {
                    _error.value = "Error al buscar el evento: ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = "Excepción al buscar evento: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}