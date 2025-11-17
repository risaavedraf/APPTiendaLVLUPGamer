package com.example.tiendalvlupgamer.viewmodel

import com.example.tiendalvlupgamer.data.repository.CarritoRepository
import com.example.tiendalvlupgamer.data.repository.PedidoRepository
import com.example.tiendalvlupgamer.model.AddItemRequest
import com.example.tiendalvlupgamer.model.CarritoResponse
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import retrofit2.Response

@ExperimentalCoroutinesApi
class CartViewModelTest : StringSpec({

     val carritoRepository: CarritoRepository = mockk()
     val pedidoRepository: PedidoRepository = mockk()
     lateinit var viewModel: CartViewModel

    beforeTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = CartViewModel(carritoRepository, pedidoRepository, cartDao)
    }

    "loadCarrito should update cartState" {
        val carritoResponse = CarritoResponse(emptyList(), 0.0, 0.0, 0.0)
        coEvery { carritoRepository.getCarrito() } returns Response.success(carritoResponse)

        viewModel.loadCarrito()
        viewModel.cartState.observeForever {  }

        viewModel.cartState.value shouldBe carritoResponse
    }

    "addItem should update cartState" {
        val request = AddItemRequest(1, 1)
        val carritoResponse = CarritoResponse(emptyList(), 0.0, 0.0, 0.0)
        coEvery { carritoRepository.addItem(request) } returns Response.success(carritoResponse)

        viewModel.addItem(1, 1)
        viewModel.cartState.observeForever {  }

        viewModel.cartState.value shouldBe carritoResponse
    }
})