package com.example.tiendalvlupgamer.data.repository

import com.example.tiendalvlupgamer.data.network.ImagenApiService
import okhttp3.MultipartBody

class ImagenRepository(private val imagenApiService: ImagenApiService) {

    suspend fun getUsuarioImagenBase64(usuarioId: Long, imageId: Long) = 
        imagenApiService.getUsuarioImagenBase64(usuarioId, imageId)

    suspend fun getProductoImagenBase64(productoId: Long, imageId: Long) =
        imagenApiService.getProductoImagenBase64(productoId, imageId)

    suspend fun getEventoImagenBase64(eventoId: Long, imageId: Long) = 
        imagenApiService.getEventoImagenBase64(eventoId, imageId)

    suspend fun subirImagenUsuario(usuarioId: Long, file: MultipartBody.Part) = 
        imagenApiService.subirImagenUsuario(usuarioId, file)
}
