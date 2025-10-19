package com.example.tiendalvlupgamer.model.repository

import com.example.tiendalvlupgamer.model.local.ProductDao
import com.example.tiendalvlupgamer.model.local.ProductEntity
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val dao: ProductDao) {

    fun observarTodos(): Flow<List<ProductEntity>> = dao.observarTodos()

    suspend fun obtenerPorId(id: String) = dao.obtenerPorId(id)



}