package com.example.tiendalvlupgamer.model.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ProductDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var productDao: ProductDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        productDao = database.productDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetProducts() = runTest {
        val products = listOf(ProductEntity("1", "category", "name", 10, "description", 0))
        productDao.insertar(products)
        val retrievedProducts = productDao.observarTodos().first()
        assertThat(retrievedProducts).isEqualTo(products)
    }

    @Test
    fun getProductById() = runTest {
        val product = ProductEntity("1", "category", "name", 10, "description", 0)
        productDao.insertar(listOf(product))
        val retrievedProduct = productDao.obtenerPorId("1")
        assertThat(retrievedProduct).isEqualTo(product)
    }

    @Test
    fun searchProducts() = runTest {
        val product = ProductEntity("1", "category", "test_product", 10, "description", 0)
        productDao.insertar(listOf(product))
        val searchResult = productDao.buscarProductos("test").first()
        assertThat(searchResult).hasSize(1)
        assertThat(searchResult[0]).isEqualTo(product)
    }
}