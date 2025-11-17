package com.example.tiendalvlupgamer.model

import com.google.gson.annotations.SerializedName

/**
 * Representa la estructura de error estándar devuelta por la API.
 */
data class ApiErrorResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("error") val error: String,
    @SerializedName("message") val message: String,
    @SerializedName("path") val path: String
)
