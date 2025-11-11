package com.example.tiendalvlupgamer.data.network

import com.example.tiendalvlupgamer.model.AddItemRequest
import com.example.tiendalvlupgamer.model.CarritoResponse
import com.example.tiendalvlupgamer.model.CuponRequest
import com.example.tiendalvlupgamer.model.UpdateQuantityRequest
import retrofit2.Response
import retrofit2.http.*

interface CarritoApiService {

    @GET("carrito")
    suspend fun getCarrito(): Response<CarritoResponse>

    @POST("carrito/items")
    suspend fun addItem(@Body addItemRequest: AddItemRequest): Response<CarritoResponse>

    @PUT("carrito/items/{productoId}")
    suspend fun updateItemQuantity(
        @Path("productoId") productoId: Long,
        @Body updateQuantityRequest: UpdateQuantityRequest
    ): Response<CarritoResponse>

    @DELETE("carrito/items/{productoId}")
    suspend fun deleteItem(@Path("productoId") productoId: Long): Response<CarritoResponse>

    @DELETE("carrito")
    suspend fun limpiarCarrito(): Response<Unit>

    @POST("carrito/cupon")
    suspend fun aplicarCupon(@Body cuponRequest: CuponRequest): Response<CarritoResponse>

    @DELETE("carrito/cupon")
    suspend fun quitarCupon(): Response<CarritoResponse>
}
