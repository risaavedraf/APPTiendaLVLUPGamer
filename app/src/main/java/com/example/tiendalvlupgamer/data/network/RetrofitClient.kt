package com.example.tiendalvlupgamer.data.network


import com.example.tiendalvlupgamer.util.SessionManager
import com.google.gson.GsonBuilder
import com.yalantis.ucrop.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime

object RetrofitClient {
    const val API_BASE_URL = "http://13.220.165.82:8080"
    private const val BASE_URL = "$API_BASE_URL/api/"

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().apply {
        addInterceptor { chain ->
            val token = SessionManager.getToken()
            val request = if (token != null) {
                chain.request().newBuilder().addHeader("Authorization", "Bearer $token").build()
            } else {
                chain.request()
            }
            chain.proceed(request)
        }
        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            addInterceptor(logging)
        }
    }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    val profileApiService: ProfileApiService by lazy {
        retrofit.create(ProfileApiService::class.java)
    }

    val direccionApiService: DireccionApiService by lazy {
        retrofit.create(DireccionApiService::class.java)
    }

    val productoApiService: ProductoApiService by lazy {
        retrofit.create(ProductoApiService::class.java)
    }

    val reviewApiService: ReviewApiService by lazy {
        retrofit.create(ReviewApiService::class.java)
    }

    val pedidoApiService: PedidoApiService by lazy {
        retrofit.create(PedidoApiService::class.java)
    }
    
    val carritoApiService: CarritoApiService by lazy {
        retrofit.create(CarritoApiService::class.java)
    }

    val eventoApiService: EventoApiService by lazy {
        retrofit.create(EventoApiService::class.java)
    }

    val imagenApiService: ImagenApiService by lazy {
        retrofit.create(ImagenApiService::class.java)
    }
}
