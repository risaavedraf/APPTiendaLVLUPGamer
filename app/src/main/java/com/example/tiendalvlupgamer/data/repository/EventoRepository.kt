package com.example.tiendalvlupgamer.data.repository

import com.example.tiendalvlupgamer.data.network.EventoApiService
import com.example.tiendalvlupgamer.model.EventoResponse
import com.example.tiendalvlupgamer.model.PageResponse
import retrofit2.Response

class EventoRepository(private val eventoApiService: EventoApiService) {

    suspend fun getEventos(
        query: String? = null,
        page: Int = 0,
        size: Int = 10
    ): Response<PageResponse<EventoResponse>> {
        return eventoApiService.getEventos(query = query, page = page, size = size)
    }

    suspend fun getEventoById(id: Long): Response<EventoResponse> {
        return eventoApiService.getEventoById(id)
    }
}
