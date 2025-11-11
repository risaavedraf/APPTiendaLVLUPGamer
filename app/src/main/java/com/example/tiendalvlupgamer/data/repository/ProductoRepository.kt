package com.example.tiendalvlupgamer.data.repository

import com.example.tiendalvlupgamer.data.network.ProductoApiService

class ProductoRepository(private val productoApiService: ProductoApiService) {

    suspend fun getProductos(page: Int, size: Int, categoriaId: Long? = null) = productoApiService.getProductos(page, size, categoriaId)

    suspend fun getProductoById(id: Long) = productoApiService.getProductoById(id)

    suspend fun searchProductos(query: String, page: Int, size: Int) = productoApiService.searchProductos(query, page, size)

    suspend fun getImageData(imageUrl: String) = productoApiService.getImageData(imageUrl)

    suspend fun getCategorias() = productoApiService.getCategorias()
}
