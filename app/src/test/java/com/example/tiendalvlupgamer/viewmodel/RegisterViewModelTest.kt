package com.example.tiendalvlupgamer.viewmodel

import com.example.tiendalvlupgamer.data.dao.UserDao
import com.example.tiendalvlupgamer.data.repository.AuthRepository
import com.example.tiendalvlupgamer.model.MessageResponse
import com.example.tiendalvlupgamer.model.RegistroRequest
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain

@ExperimentalCoroutinesApi
class RegisterViewModelTest : StringSpec({

     val userDao: UserDao = mockk(relaxed = true)
     val authRepository: AuthRepository = mockk()
     lateinit var viewModel: RegisterViewModel

    beforeTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = RegisterViewModel(userDao, authRepository)
    }

    "onNameChange should update uiState" {
        viewModel.onNameChange("Test")
        viewModel.uiState.name shouldBe "Test"
    }

    "tryRegister with valid data should succeed" {
        coEvery { authRepository.register(any()) } returns MessageResponse("Success")

        viewModel.onNameChange("Test")
        viewModel.onLastNameChange("User")
        viewModel.onUsernameChange("testuser")
        viewModel.onBirthDateChange("01/01/2000")
        viewModel.onEmailChange("test@example.com")
        viewModel.onPasswordChange("Password123")
        viewModel.tryRegister { }

        viewModel.uiState.registrationSuccess shouldBe true
    }

    "tryRegister with invalid data should fail" {
        viewModel.onNameChange("")
        viewModel.tryRegister { }

        viewModel.uiState.error shouldBe "El nombre es requerido"
    }
})