package com.example.tiendalvlupgamer.model

import java.time.LocalDate
import java.time.LocalDateTime

data class FullProfileResponse(
    val id: Long,
    val username: String,
    val email: String,
    val roles: Set<String>,
    val name: String,
    val lastName: String,
    val birthDate: LocalDate?,
    val profileImageBase64: String?,
    val direcciones: List<DireccionResponse>,
    val reviews: List<ReviewResponse>
)
