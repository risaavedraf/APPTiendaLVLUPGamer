package com.example.tiendalvlupgamer.data.repository

import com.example.tiendalvlupgamer.data.network.CarritoApiService
import com.example.tiendalvlupgamer.model.AddItemRequest
import com.example.tiendalvlupgamer.model.CuponRequest
import com.example.tiendalvlupgamer.model.UpdateQuantityRequest

class CarritoRepository(private val carritoApiService: CarritoApiService) {

    suspend fun getCarrito() = carritoApiService.getCarrito()

    suspend fun addItem(addItemRequest: AddItemRequest) = carritoApiService.addItem(addItemRequest)

    suspend fun updateItemQuantity(productoId: Long, updateQuantityRequest: UpdateQuantityRequest) = 
        carritoApiService.updateItemQuantity(productoId, updateQuantityRequest)

    suspend fun deleteItem(productoId: Long) = carritoApiService.deleteItem(productoId)

    suspend fun limpiarCarrito() = carritoApiService.limpiarCarrito()

    suspend fun aplicarCupon(cuponRequest: CuponRequest) = carritoApiService.aplicarCupon(cuponRequest)

    suspend fun quitarCupon() = carritoApiService.quitarCupon()
}
