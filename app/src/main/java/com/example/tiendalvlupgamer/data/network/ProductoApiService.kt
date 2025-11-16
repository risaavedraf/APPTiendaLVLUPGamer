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
        @Query("query") query: String? = null,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("categoriaId") categoriaId: Long? = null,
        @Query("sort") sort: String = "nombre,asc"
    ): Response<PageResponse<ProductoResponse>>

    @GET("productos/{id}")
    suspend fun getProductoById(@Path("id") id: Long): Response<ProductoResponse>

    @GET
    suspend fun getImageData(@Url imageUrl: String): Response<ImageResponse>

    @GET("categorias")
    suspend fun getCategorias(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<PageResponse<CategoriaResponse>>
}
