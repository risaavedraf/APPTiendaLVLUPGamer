package com.example.tiendalvlupgamer.viewmodel

import com.example.tiendalvlupgamer.data.dao.UserDao
import com.example.tiendalvlupgamer.data.repository.AuthRepository
import com.example.tiendalvlupgamer.model.LoginResponse
import com.example.tiendalvlupgamer.model.UsuarioResponse
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import java.time.LocalDate
import java.time.LocalDateTime

@ExperimentalCoroutinesApi
class LoginViewModelTest : StringSpec({

     val userDao: UserDao = mockk()
     val authRepository: AuthRepository = mockk()
     lateinit var viewModel: LoginViewModel

    beforeTest {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = LoginViewModel(userDao, authRepository)
    }

    "onEmailOrUsernameChange should update uiState" {
        viewModel.onEmailOrUsernameChange("test@example.com")
        viewModel.uiState.emailOrUsername shouldBe "test@example.com"
    }

    "onPasswordChange should update uiState" {
        viewModel.onPasswordChange("password")
        viewModel.uiState.password shouldBe "password"
    }

    "tryLogin with valid credentials should succeed" {
        val usuarioResponse = UsuarioResponse(1, "test", "test@example.com", emptySet(), "Test", "User", LocalDate.now(), null, LocalDateTime.now(), LocalDateTime.now())
        val loginResponse = LoginResponse("token", usuarioResponse)
        coEvery { authRepository.login(any(), any()) } returns loginResponse

        viewModel.onEmailOrUsernameChange("test@example.com")
        viewModel.onPasswordChange("password")
        viewModel.tryLogin {  }

        viewModel.uiState.loginSuccess shouldBe true
    }

    "tryLogin with invalid credentials should fail" {
        coEvery { authRepository.login(any(), any()) } throws Exception("Invalid credentials")

        viewModel.onEmailOrUsernameChange("test@example.com")
        viewModel.onPasswordChange("wrong_password")
        viewModel.tryLogin {  }

        viewModel.uiState.error shouldBe "Error al iniciar sesión: Invalid credentials"
    }
})