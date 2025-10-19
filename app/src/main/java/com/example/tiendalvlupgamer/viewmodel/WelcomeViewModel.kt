package com.example.tiendalvlupgamer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendalvlupgamer.model.WelcomeNavigation
import com.example.tiendalvlupgamer.model.WelcomeOption
import com.example.tiendalvlupgamer.model.WelcomeState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WelcomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(WelcomeState())
    val uiState = _uiState.asStateFlow()

    private val _navigation = MutableSharedFlow<WelcomeNavigation>()
    val navigation = _navigation.asSharedFlow()

    fun onRegister() {
        _uiState.value = _uiState.value.copy(selected = WelcomeOption.REGISTER)
        viewModelScope.launch { _navigation.emit(WelcomeNavigation.ToRegister) }
    }

    fun onLogin() {
        _uiState.value = _uiState.value.copy(selected = WelcomeOption.LOGIN)
        viewModelScope.launch { _navigation.emit(WelcomeNavigation.ToLogin) }
    }

    fun onGuest() {
        _uiState.value = _uiState.value.copy(selected = WelcomeOption.GUEST)
        viewModelScope.launch { _navigation.emit(WelcomeNavigation.ToGuest) }
    }
}