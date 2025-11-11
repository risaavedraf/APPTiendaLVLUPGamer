package com.example.tiendalvlupgamer.model

import com.google.gson.annotations.SerializedName

data class ReviewRequest(
    @SerializedName("calificacion") val calificacion: Int,
    @SerializedName("comentario") val comentario: String
)
