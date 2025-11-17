package com.example.tiendalvlupgamer.viewmodel

import com.example.tiendalvlupgamer.data.repository.EventoRepository
import com.example.tiendalvlupgamer.model.EventoResponse
import com.example.tiendalvlupgamer.model.PageResponse
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
class EventsViewModelTest : StringSpec({


     val eventoRepository: EventoRepository = mockk()
     lateinit var viewModel: EventsViewModel

    beforeTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = EventsViewModel(eventoRepository)
    }

    "loadEvents should update events" {
        val eventoResponse = EventoResponse(1, "name", "description", LocalDateTime.now(), "location", 0.0, 0.0, null, LocalDateTime.now(), LocalDateTime.now())
        val pageResponse = PageResponse(listOf(eventoResponse), 0, 1, 1, 1, true)
        coEvery { eventoRepository.getEventos() } returns Response.success(pageResponse)

        viewModel.loadEvents()

        viewModel.events.value shouldBe listOf(eventoResponse)
    }

    "getEventById should update selectedEvent" {
        val eventoResponse = EventoResponse(1, "name", "description", LocalDateTime.now(), "location", 0.0, 0.0, null, LocalDateTime.now(), LocalDateTime.now())
        coEvery { eventoRepository.getEventoById(1) } returns Response.success(eventoResponse)

        viewModel.getEventById(1)

        viewModel.selectedEvent.value shouldBe eventoResponse
    }
})