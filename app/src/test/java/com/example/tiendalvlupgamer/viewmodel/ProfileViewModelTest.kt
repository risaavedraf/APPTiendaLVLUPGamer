package com.example.tiendalvlupgamer.viewmodel

import com.example.tiendalvlupgamer.data.repository.ImagenRepository
import com.example.tiendalvlupgamer.data.repository.ProfileRepository
import com.example.tiendalvlupgamer.model.FullProfileResponse
import com.example.tiendalvlupgamer.model.UpdateProfileRequest
import com.example.tiendalvlupgamer.model.UsuarioResponse
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class ProfileViewModelTest : StringSpec({



     val profileRepository: ProfileRepository = mockk()
     val imagenRepository: ImagenRepository = mockk()
     lateinit var viewModel: ProfileViewModel

    beforeTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = ProfileViewModel(profileRepository, imagenRepository)
    }

    "getMyProfile should update profile" {
        val profileResponse = FullProfileResponse(1, "username", "email", emptySet(), "name", "lastName", LocalDate.now(), null, emptyList(), emptyList())
        coEvery { profileRepository.getMyProfile() } returns Response.success(profileResponse)

        viewModel.getMyProfile()
        viewModel.profile.observeForever {  }

        viewModel.profile.value shouldBe profileResponse
    }

    "updateMyProfile should update updateResult" {
        val request = UpdateProfileRequest("name", "lastName", "2000-01-01")
        val usuarioResponse = UsuarioResponse(1, "username", "email", emptySet(), "name", "lastName", LocalDate.now(), null, LocalDateTime.now(), LocalDateTime.now())
        coEvery { profileRepository.updateMyProfile(request) } returns Response.success(usuarioResponse)

        viewModel.updateMyProfile("name", "lastName", "2000-01-01")
        viewModel.updateResult.observeForever {  }

        viewModel.updateResult.value shouldBe usuarioResponse
    }
})