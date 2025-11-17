package com.example.tiendalvlupgamer.model

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

// --- Request Models ---

data class CheckoutRequest(
    @SerializedName("direccionId") val direccionId: Long,
    @SerializedName("items") val items: List<CheckoutItemRequest>
)

data class CheckoutItemRequest(
    @SerializedName("productId") val productId: Long,
    @SerializedName("quantity") val quantity: Int
)

// --- Response Models ---

data class PedidoResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("fechaPedido") val fechaPedido: LocalDateTime,
    @SerializedName("total") val total: Double,
    @SerializedName("estado") val estado: String,
    @SerializedName("direccion") val direccion: DireccionResponse,
    @SerializedName("detalles") val detalles: List<DetallePedidoResponse>,
    @SerializedName("usuario") val usuario: UsuarioPedidoResponse
)

data class DetallePedidoResponse(
    @SerializedName("productId") val productId: Long,
    @SerializedName("productName") val productName: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("unitPrice") val unitPrice: Double,
    @SerializedName("subtotal") val subtotal: Double
)

data class UsuarioPedidoResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("username") val username: String
)
