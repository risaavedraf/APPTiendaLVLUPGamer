package com.example.tiendalvlupgamer.viewmodel

import com.example.tiendalvlupgamer.data.repository.ProductoRepository
import com.example.tiendalvlupgamer.model.CategoriaResponse
import com.example.tiendalvlupgamer.model.PageResponse
import com.example.tiendalvlupgamer.model.ProductoResponse
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
class HomeViewModelTest : StringSpec({

     val productoRepository: ProductoRepository = mockk()
     lateinit var viewModel: HomeViewModel

    beforeTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = HomeViewModel(productoRepository)
    }

    "loadHomePageContent should update homeState" {
        val categoria = CategoriaResponse(1, "PC", LocalDateTime.now(), LocalDateTime.now())
        val producto = ProductoResponse(1, "Laptop", "description", 100.0, 10, categoria, null, LocalDateTime.now(), LocalDateTime.now())
        val pageResponse = PageResponse(listOf(producto), 0, 1, 1, 1, true)
        val categoriaResponse = PageResponse(listOf(categoria), 0, 1, 1, 1, true)
        
        coEvery { productoRepository.getCategorias() } returns Response.success(categoriaResponse)
        coEvery { productoRepository.getProductos(any(), any(), any(), any()) } returns Response.success(pageResponse)

        viewModel.homeState.observeForever {  }

        viewModel.homeState.value?.size shouldBe 1
        viewModel.homeState.value?.get(0)?.categoria shouldBe categoria
    }
})