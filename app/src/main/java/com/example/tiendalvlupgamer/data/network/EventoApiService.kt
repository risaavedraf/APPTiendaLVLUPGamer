package com.example.tiendalvlupgamer.data.network

import com.example.tiendalvlupgamer.model.EventoResponse
import com.example.tiendalvlupgamer.model.PageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventoApiService {

    @GET("eventos")
    suspend fun getEventos(
        @Query("query") query: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sort") sort: String = "date,asc"
    ): Response<PageResponse<EventoResponse>>

    @GET("eventos/{id}")
    suspend fun getEventoById(@Path("id") id: Long): Response<EventoResponse>
}
