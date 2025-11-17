package com.example.tiendalvlupgamer.data.repository

import com.example.tiendalvlupgamer.data.network.PedidoApiService
import com.example.tiendalvlupgamer.model.CheckoutRequest

class PedidoRepository(private val pedidoApiService: PedidoApiService) {

    suspend fun realizarCheckout(checkoutRequest: CheckoutRequest) = pedidoApiService.realizarCheckout(checkoutRequest)

    suspend fun getMisPedidos() = pedidoApiService.getMisPedidos()
}
