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
class CartDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: AppDatabase
    private lateinit var cartDao: CartDao
    private lateinit var productDao: ProductDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        cartDao = database.cartDao()
        productDao = database.productDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndGetCartItems() = runTest {
        val product = ProductEntity("1", "category", "name", 10, "description", 0)
        productDao.insertar(listOf(product))
        val cartItem = CartItemEntity("1", 2)
        cartDao.insert(cartItem)
        val retrievedCartItems = cartDao.getCartItems().first()
        assertThat(retrievedCartItems).hasSize(1)
        assertThat(retrievedCartItems[0].product).isEqualTo(product)
        assertThat(retrievedCartItems[0].quantity).isEqualTo(2)
    }

    @Test
    fun clearCart() = runTest {
        val product = ProductEntity("1", "category", "name", 10, "description", 0)
        productDao.insertar(listOf(product))
        val cartItem = CartItemEntity("1", 2)
        cartDao.insert(cartItem)
        cartDao.clearCart()
        val retrievedCartItems = cartDao.getCartItems().first()
        assertThat(retrievedCartItems).isEmpty()
    }
}