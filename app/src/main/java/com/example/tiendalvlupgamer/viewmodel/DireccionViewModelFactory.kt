package com.example.tiendalvlupgamer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tiendalvlupgamer.data.repository.DireccionRepository

class DireccionViewModelFactory(private val repository: DireccionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DireccionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DireccionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
