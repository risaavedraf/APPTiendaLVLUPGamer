package com.example.tiendalvlupgamer.data.network

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Url

interface ImagenApiService {

    @GET
    suspend fun descargarImagen(@Url url: String): Response<ResponseBody>

    @GET("/api/usuarios/{id}/images/{imageId}/base64")
    suspend fun getUsuarioImagenBase64(
        @Path("id") usuarioId: Long,
        @Path("imageId") imageId: Long
    ): Response<String>

    @GET("/api/productos/{productoId}/imagenes/{imageId}/base64")
    suspend fun getProductoImagenBase64(
        @Path("productoId") productoId: Long,
        @Path("imageId") imageId: Long
    ): Response<String>

    @GET("/api/eventos/{eventoId}/imagenes/{imageId}/base64")
    suspend fun getEventoImagenBase64(
        @Path("eventoId") eventoId: Long,
        @Path("imageId") imageId: Long
    ): Response<String>

    @Multipart
    @POST("/api/usuarios/{id}/images")
    suspend fun subirImagenUsuario(
        @Path("id") usuarioId: Long,
        @Part file: MultipartBody.Part
    ): Response<ResponseBody>
}
