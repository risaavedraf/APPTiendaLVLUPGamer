package com.example.tiendalvlupgamer.model

import java.time.LocalDateTime

data class ReviewResponse(
    val id: Long,
    val calificacion: Int,
    val comentario: String,
    val fecha: LocalDateTime,
    val author: String,
    val authorId: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
