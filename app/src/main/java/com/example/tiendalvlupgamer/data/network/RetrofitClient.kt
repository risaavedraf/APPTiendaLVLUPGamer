package com.example.tiendalvlupgamer.data.network

import com.example.tiendalvlupgamer.util.SessionManager
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:8080/"

    // 1. Crear una instancia de Gson con ambos adaptadores registrados
    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val token = SessionManager.getToken()
            val request = if (token != null) {
                chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
            } else {
                chain.request()
            }
            chain.proceed(request)
        }
        .build()

    val instance: AuthApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            // 2. Usar la instancia de Gson personalizada
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        retrofit.create(AuthApiService::class.java)
    }
}
