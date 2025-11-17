package com.example.tiendalvlupgamer.viewmodel


import com.example.tiendalvlupgamer.data.repository.PedidoRepository
import com.example.tiendalvlupgamer.model.PedidoResponse
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import retrofit2.Response
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class PedidosViewModelTest : StringSpec({



     val pedidoRepository: PedidoRepository = mockk()
     lateinit var viewModel: PedidosViewModel

    beforeTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = PedidosViewModel(pedidoRepository)
    }

    "loadMisPedidos should update pedidos" {
        val pedidoResponse = PedidoResponse(1, LocalDateTime.now(), 100.0, "PENDIENTE", mockk(), emptyList(), mockk())
        coEvery { pedidoRepository.getMisPedidos() } returns Response.success(listOf(pedidoResponse))

        viewModel.loadMisPedidos()
        viewModel.pedidos.observeForever {  }

        viewModel.pedidos.value shouldBe listOf(pedidoResponse)
    }
})