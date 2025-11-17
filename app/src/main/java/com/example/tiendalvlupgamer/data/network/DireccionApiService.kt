package com.example.tiendalvlupgamer.data.network

import com.example.tiendalvlupgamer.model.DireccionRequest
import com.example.tiendalvlupgamer.model.DireccionResponse
import retrofit2.Response
import retrofit2.http.*

interface DireccionApiService {

    @GET("direcciones")
    suspend fun getMisDirecciones(): Response<List<DireccionResponse>>

    @GET("direcciones/{id}")
    suspend fun getDireccionById(@Path("id") id: Long): Response<DireccionResponse>

    @POST("direcciones")
    suspend fun createDireccion(@Body direccionRequest: DireccionRequest): Response<DireccionResponse>

    @PUT("direcciones/{id}")
    suspend fun updateDireccion(
        @Path("id") id: Long,
        @Body direccionRequest: DireccionRequest
    ): Response<DireccionResponse>

    @DELETE("direcciones/{id}")
    suspend fun deleteDireccion(@Path("id") id: Long): Response<Unit>
}
