package com.example.tiendalvlupgamer.data.network

import com.example.tiendalvlupgamer.model.CategoriaResponse
import com.example.tiendalvlupgamer.model.ImageResponse
import com.example.tiendalvlupgamer.model.PageResponse
import com.example.tiendalvlupgamer.model.ProductoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ProductoApiService {

    @GET("productos")
    suspend fun getProductos(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("categoriaId") categoriaId: Long? = null, // 1. Añadido el filtro opcional
        @Query("sort") sort: String = "nombre,asc"
    ): Response<PageResponse<ProductoResponse>>

    @GET("productos/{id}")
    suspend fun getProductoById(@Path("id") id: Long): Response<ProductoResponse>

    @GET("productos/search")
    suspend fun searchProductos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<PageResponse<ProductoResponse>>

    @GET
    suspend fun getImageData(@Url imageUrl: String): Response<ImageResponse>

    // 2. Añadido el nuevo endpoint para obtener categorías
    @GET("categorias")
    suspend fun getCategorias(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20 // Obtenemos hasta 20 categorías, debería ser suficiente
    ): Response<PageResponse<CategoriaResponse>>
}
