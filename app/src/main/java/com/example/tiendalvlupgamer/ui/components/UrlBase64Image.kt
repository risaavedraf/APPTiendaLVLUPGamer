package com.example.tiendalvlupgamer.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.example.tiendalvlupgamer.util.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

@Composable
fun UrlBase64Image(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String?,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: @Composable () -> Unit
) {
    var base64Data by remember(imageUrl) { mutableStateOf<String?>(null) }

    LaunchedEffect(imageUrl) {
        if (imageUrl.isNullOrBlank()) {
            return@LaunchedEffect
        }

        base64Data = withContext(Dispatchers.IO) {
            try {
                val client = OkHttpClient.Builder()
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

                val request = Request.Builder().url(imageUrl).build()
                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    response.body?.string()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    Base64Image(
        base64String = base64Data,
        modifier = modifier,
        contentDescription = contentDescription,
        contentScale = contentScale,
        placeholder = placeholder
    )
}
