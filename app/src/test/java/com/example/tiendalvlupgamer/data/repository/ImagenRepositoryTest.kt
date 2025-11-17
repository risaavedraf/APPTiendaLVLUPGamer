package com.example.tiendalvlupgamer.data.repository

import com.example.tiendalvlupgamer.data.network.ImagenApiService
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class ImagenRepositoryTest : StringSpec({

    val apiService: ImagenApiService = mockk()
    val repository = ImagenRepository(apiService)

    "descargarImagen should return response body" {
        val responseBody = "content".toResponseBody("text/plain".toMediaTypeOrNull())
        coEvery { apiService.descargarImagen(any()) } returns Response.success(responseBody)

        val result = repository.descargarImagen("url")

        result.body() shouldBe responseBody
    }

    "getUsuarioImagenBase64 should return base64 string" {
        val base64 = "base64string"
        coEvery { apiService.getUsuarioImagenBase64(1, 1) } returns Response.success(base64)

        val result = repository.getUsuarioImagenBase64(1, 1)

        result.body() shouldBe base64
    }

    "getProductoImagenBase64 should return base64 string" {
        val base64 = "base64string"
        coEvery { apiService.getProductoImagenBase64(1, 1) } returns Response.success(base64)

        val result = repository.getProductoImagenBase64(1, 1)

        result.body() shouldBe base64
    }

    "getEventoImagenBase64 should return base64 string" {
        val base64 = "base64string"
        coEvery { apiService.getEventoImagenBase64(1, 1) } returns Response.success(base64)

        val result = repository.getEventoImagenBase64(1, 1)

        result.body() shouldBe base64
    }

    "subirImagenUsuario should return success response" {
        val requestBody = "test content".toRequestBody("image/jpeg".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", "test.jpg", requestBody)
        val responseBody = "success".toResponseBody("text/plain".toMediaTypeOrNull())
        
        coEvery { apiService.subirImagenUsuario(1, filePart) } returns Response.success(responseBody)

        val result = repository.subirImagenUsuario(1, filePart)

        result.body() shouldBe responseBody
    }
})