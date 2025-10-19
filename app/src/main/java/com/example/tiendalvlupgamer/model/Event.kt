package com.example.tiendalvlupgamer.model

/**
 * Representa un evento gamer, conteniendo toda su información relevante.
 */
data class Event(
    val id: String,
    val name: String,
    val description: String,
    val date: String, // Ejemplo: "Sábado, 25 de Diciembre - 18:00 hrs"
    val locationName: String, // Ejemplo: "Centro de Eventos GamerX"
    val latitude: Double, // Coordenada para Google Maps
    val longitude: Double, // Coordenada para Google Maps
    val imageUrl: String // URL de una imagen representativa
)