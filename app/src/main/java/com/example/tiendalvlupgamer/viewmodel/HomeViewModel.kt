package com.example.tiendalvlupgamer.ui

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.random.Random

class HomeViewModel : ViewModel() {

    // Estado que la UI observará. Lo hacemos público para que la View lo pueda leer.
    var cont by mutableStateOf(0)
    var limit by mutableStateOf(Random.nextInt(5, 15))
    var showMessage by mutableStateOf(false)

    // Eventos que la View llamará
    fun incrementCounter() {
        if (cont < limit) {
            cont++
        } else {
            cont = 0
            showMessage = true
        }
    }

    fun resetMessageAndLimit() {
        showMessage = false
        limit = Random.nextInt(5, 15)
    }
}