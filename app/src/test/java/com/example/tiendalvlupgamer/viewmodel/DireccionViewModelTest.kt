package com.example.tiendalvlupgamer.viewmodel

import com.example.tiendalvlupgamer.data.repository.DireccionRepository
import com.example.tiendalvlupgamer.model.DireccionRequest
import com.example.tiendalvlupgamer.model.DireccionResponse
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
class DireccionViewModelTest : StringSpec({



     val direccionRepository: DireccionRepository = mockk()
     lateinit var viewModel: DireccionViewModel

    beforeTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = DireccionViewModel(direccionRepository)
    }

    "loadDirecciones should update direcciones" {
        val direccionResponse = DireccionResponse(1, "nombre", "destinatario", "calle", "123", null, "comuna", "ciudad", "region", "12345", LocalDateTime.now(), LocalDateTime.now())
        coEvery { direccionRepository.getMisDirecciones() } returns Response.success(listOf(direccionResponse))

        viewModel.loadDirecciones()
        viewModel.direcciones.observeForever {  }

        viewModel.direcciones.value shouldBe listOf(direccionResponse)
    }

    "createDireccion should update operationSuccess" {
        val request = DireccionRequest("nombre", "destinatario", "calle", "123", null, "comuna", "ciudad", "region", "12345")
        coEvery { direccionRepository.createDireccion(request) } returns Response.success(mockk())

        viewModel.createDireccion(request)
        viewModel.operationSuccess.observeForever {  }

        viewModel.operationSuccess.value shouldBe true
    }
})