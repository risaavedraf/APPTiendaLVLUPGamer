package com.example.tiendalvlupgamer.data.repository

import com.example.tiendalvlupgamer.data.network.AuthApiService
import com.example.tiendalvlupgamer.model.LoginRequest
import com.example.tiendalvlupgamer.model.RegistroRequest
import com.example.tiendalvlupgamer.model.LoginResponse
import com.example.tiendalvlupgamer.model.UsuarioResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

class AuthRepositoryTest {

    private val apiService: AuthApiService = mockk()
    private val authRepository = AuthRepository(apiService)

    @Test
    fun `login should return token on success`() = runBlocking {
        val loginRequest = LoginRequest("test@example.com", "password")
        val mockUserResponse = UsuarioResponse(1L, "test", "test@example.com", emptySet(), "Test", "User", LocalDate.now(), null, LocalDateTime.now(), LocalDateTime.now())
        val expectedResponse = LoginResponse("fake_token", mockUserResponse)
        coEvery { apiService.login(loginRequest) } returns expectedResponse

        val result = authRepository.login(loginRequest.email, loginRequest.password)

        assertEquals(expectedResponse, result)
    }

    @Test
    fun `register should return success message`() = runBlocking {
        val registroRequest = RegistroRequest("test", "User", "testuser", "2000-01-01", "test@example.com", "password")
        val expectedResponse = mockk<UsuarioResponse>() // El endpoint de registro ahora devuelve UsuarioResponse
        coEvery { apiService.register(registroRequest) } returns expectedResponse

        val result = authRepository.register(registroRequest)

        assertEquals(expectedResponse, result)
    }
}
