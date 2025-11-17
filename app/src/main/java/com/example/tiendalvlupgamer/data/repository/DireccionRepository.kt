package com.example.tiendalvlupgamer.data.repository

import com.example.tiendalvlupgamer.data.network.DireccionApiService
import com.example.tiendalvlupgamer.model.DireccionRequest

class DireccionRepository(private val direccionApiService: DireccionApiService) {

    suspend fun getMisDirecciones() = direccionApiService.getMisDirecciones()

    suspend fun getDireccionById(id: Long) = direccionApiService.getDireccionById(id)

    suspend fun createDireccion(request: DireccionRequest) = direccionApiService.createDireccion(request)

    suspend fun updateDireccion(id: Long, request: DireccionRequest) = direccionApiService.updateDireccion(id, request)

    suspend fun deleteDireccion(id: Long) = direccionApiService.deleteDireccion(id)
}
