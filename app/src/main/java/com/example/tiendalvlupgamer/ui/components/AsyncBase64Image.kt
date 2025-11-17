package com.example.tiendalvlupgamer.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.example.tiendalvlupgamer.data.repository.ImagenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun AsyncBase64Image(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    contentDescription: String?,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: @Composable () -> Unit
) {
    var base64String by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(imageUrl) {
        if (imageUrl.isNullOrBlank()) {
            isLoading = false
            return@LaunchedEffect
        }

        isLoading = true
        base64String = try {
            withContext(Dispatchers.IO) {
                val repository = ImagenRepository(com.example.tiendalvlupgamer.data.network.RetrofitClient.imagenApiService)
                val response = repository.descargarImagen(imageUrl)
                if (response.isSuccessful) {
                    response.body()?.string()
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            null
        } finally {
            isLoading = false
        }
    }

    if (isLoading || base64String == null) {
        placeholder()
    } else {
        Base64Image(
            base64String = base64String,
            modifier = modifier,
            contentDescription = contentDescription,
            contentScale = contentScale,
            placeholder = placeholder
        )
    }
}
