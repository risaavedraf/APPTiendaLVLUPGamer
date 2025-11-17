package com.example.tiendalvlupgamer.model

import com.google.gson.annotations.SerializedName

// --- Response Models ---

data class CarritoResponse(
    @SerializedName("items") val items: List<CarritoItemResponse>,
    @SerializedName("subtotal") val subtotal: Double,
    @SerializedName("descuento") val descuento: Double,
    @SerializedName("total") val total: Double
)

data class CarritoItemResponse(
    @SerializedName("productoId") val productoId: Long,
    @SerializedName("nombreProducto") val nombreProducto: String,
    @SerializedName("imagenUrl") val imagenUrl: String?,
    @SerializedName("precioUnitario") val precioUnitario: Double,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("subtotal") val subtotalItem: Double
)

// --- Request Models ---

data class AddItemRequest(
    @SerializedName("productoId") val productoId: Long,
    @SerializedName("cantidad") val cantidad: Int
)

data class UpdateQuantityRequest(
    @SerializedName("cantidad") val cantidad: Int
)

data class CuponRequest(
    @SerializedName("codigo") val codigo: String
)
