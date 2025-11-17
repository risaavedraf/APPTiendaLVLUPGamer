package com.example.tiendalvlupgamer.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class PageResponse<T>(
    val content: List<T>,
    val pageNumber: Int,
    val pageSize: Int,
    val totalElements: Long,
    val totalPages: Int,
    @SerializedName("last") val isLast: Boolean
)

data class ProductoResponse(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    @SerializedName("precio") val price: Double, // CORREGIDO: Se añade la anotación para el mapeo.
    val stock: Int,
    val categoria: CategoriaResponse,
    val imagenUrl: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class CategoriaResponse(
    val id: Long,
    val nombre: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
