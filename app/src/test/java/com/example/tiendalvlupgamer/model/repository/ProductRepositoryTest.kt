package com.example.tiendalvlupgamer.model.repository

import com.example.tiendalvlupgamer.model.local.ProductDao
import com.example.tiendalvlupgamer.model.local.ProductEntity
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class ProductRepositoryTest : StringSpec({

    val dao: ProductDao = mockk()
    val repository = ProductRepository(dao)

    "observarTodos should return flow of products" {
        val products = listOf(ProductEntity("1", "category", "name", 10, "description", 0))
        coEvery { dao.observarTodos() } returns flowOf(products)

        val result = repository.observarTodos().first()

        result shouldBe products
    }

    "obtenerPorId should return product" {
        val product = ProductEntity("1", "category", "name", 10, "description", 0)
        coEvery { dao.obtenerPorId("1") } returns product

        val result = repository.obtenerPorId("1")

        result shouldBe product
    }
})