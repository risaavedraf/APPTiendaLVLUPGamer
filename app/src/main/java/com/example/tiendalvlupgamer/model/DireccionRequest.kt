package com.example.tiendalvlupgamer.model

import com.google.gson.annotations.SerializedName

data class DireccionRequest(
    @SerializedName("nombre") val nombre: String,
    @SerializedName("nombreDestinatario") val nombreDestinatario: String,
    @SerializedName("calle") val calle: String,
    @SerializedName("numeroCasa") val numeroCasa: String,
    @SerializedName("numeroDepartamento") val numeroDepartamento: String?,
    @SerializedName("comuna") val comuna: String,
    @SerializedName("ciudad") val ciudad: String,
    @SerializedName("region") val region: String,
    @SerializedName("codigoPostal") val codigoPostal: String
)
