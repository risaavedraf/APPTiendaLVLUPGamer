package com.example.tiendalvlupgamer.data.network

import com.example.tiendalvlupgamer.model.CheckoutRequest
import com.example.tiendalvlupgamer.model.PedidoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PedidoApiService {

    @POST("pedidos/checkout")
    suspend fun realizarCheckout(@Body checkoutRequest: CheckoutRequest): Response<PedidoResponse>

    @GET("pedidos/me")
    suspend fun getMisPedidos(): Response<List<PedidoResponse>>
}
