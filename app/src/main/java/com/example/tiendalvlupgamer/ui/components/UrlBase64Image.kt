package com.example.tiendalvlupgamer.ui.components

import android.util.Base64
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.example.tiendalvlupgamer.data.network.RetrofitClient
import com.example.tiendalvlupgamer.data.repository.ImagenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
            Log.i("UrlBase64Image", "Image URL is null or blank (showing placeholder)")
            return@LaunchedEffect
        }

        base64Data = withContext(Dispatchers.IO) {
            try {
                val repository = ImagenRepository(RetrofitClient.imagenApiService)
                val fullUrl = if (imageUrl.startsWith("http")) imageUrl else RetrofitClient.API_BASE_URL + imageUrl
                Log.i("UrlBase64Image", "Attempting to load image from URL: $fullUrl")

                val response = repository.descargarImagen(fullUrl)

                if (response.isSuccessful) {
                    // If the backend endpoint already returns Base64 text (e.g., .../base64), read it as String.
                    if (fullUrl.contains("/base64")) {
                        val base64Text = response.body()?.string()
                        Log.i("UrlBase64Image", "Loaded Base64 text: length=${base64Text?.length ?: 0}")
                        if (!base64Text.isNullOrBlank()) base64Text else null
                    } else {
                        val imageBytes = response.body()?.bytes()
                        if (imageBytes != null) {
                            Log.i("UrlBase64Image", "Image downloaded successfully, ${imageBytes.size} bytes.")
                            Base64.encodeToString(imageBytes, Base64.DEFAULT)
                        } else {
                            Log.e("UrlBase64Image", "Image download response body is null.")
                            null
                        }
                    }
                } else {
                    Log.e("UrlBase64Image", "Error downloading image: ${response.code()} - ${response.message()}")
                    Log.e("UrlBase64Image", "Error body: ${response.errorBody()?.string()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("UrlBase64Image", "Exception while loading image: ${e.message}", e)
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
