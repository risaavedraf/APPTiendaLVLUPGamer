package com.example.tiendalvlupgamer.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tiendalvlupgamer.model.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EventsViewModel : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    // 1. StateFlow para el evento seleccionado
    private val _selectedEvent = MutableStateFlow<Event?>(null)
    val selectedEvent: StateFlow<Event?> = _selectedEvent

    init {
        loadEvents()
    }

    // 2. Función para obtener un evento por su ID
    fun getEventById(id: String) {
        _selectedEvent.value = _events.value.find { it.id == id }
    }

    private fun loadEvents() {
        _events.value = listOf(
            Event(
                id = "1",
                name = "Torneo Nacional de Valorant",
                description = "Compite contra los mejores equipos del país y demuestra tu valía en el torneo de Valorant más esperado del año. ¡Inscripciones abiertas!",
                date = "Sábado, 15 de Noviembre - 10:00 hrs",
                locationName = "Movistar Arena, Santiago",
                latitude = -33.45694,
                longitude = -70.68417,
                imageUrl = "https://images.contentstack.io/v3/assets/bltb6530b271fddd0b1/blt_6f80a93c934988e3/65383a526435372336332152/Valorant_2023_keyart_3_1920x1080.jpg"
            ),
            Event(
                id = "2",
                name = "Lanzamiento de 'Cyberpunk 2078'",
                description = "Sé el primero en jugar la nueva expansión. Habrá stands de prueba, cosplayers y sorteos exclusivos para los asistentes.",
                date = "Viernes, 28 de Noviembre - 19:00 hrs",
                locationName = "Centro Cultural Estación Mapocho, Santiago",
                latitude = -33.4318,
                longitude = -70.6553,
                imageUrl = "https://static.bandainamcoent.eu/high/cyberpunk-2077/cyberpunk-2077-phantom-liberty/00-page-setup/CP2077_PL_Key-art_with-logo_2560x1440.jpg"
            ),
            Event(
                id = "3",
                name = "Feria Retro Gamer",
                description = "Un viaje a la nostalgia. Ven a jugar, intercambiar y comprar consolas y videojuegos clásicos, desde Atari hasta PlayStation 2.",
                date = "Domingo, 7 de Diciembre - 11:00 hrs",
                locationName = "Club Hípico de Santiago",
                latitude = -33.4688,
                longitude = -70.6695,
                imageUrl = "https://static.vecteezy.com/system/resources/thumbnails/030/171/997/original/retro-gaming-controller-with-retro-color-palette-and-headphone-in-pixel-art-style-generative-ai-png.png"
            )
        )
    }
}