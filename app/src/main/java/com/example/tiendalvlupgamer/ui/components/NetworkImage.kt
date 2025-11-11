package com.example.tiendalvlupgamer.ui.components

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.example.tiendalvlupgamer.data.network.RetrofitClient
import com.example.tiendalvlupgamer.data.repository.ProductoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun NetworkImage(
    imageUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    var imageBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }

    LaunchedEffect(imageUrl) {
        if (imageUrl.isNullOrEmpty()) {
            isLoading = false
            hasError = true
            return@LaunchedEffect
        }

        isLoading = true
        hasError = false
        try {
            val repo = ProductoRepository(RetrofitClient.productoApiService)
            // La llamada a la red se hace en el dispatcher de IO
            val response = withContext(Dispatchers.IO) {
                repo.getImageData(imageUrl)
            }

            if (response.isSuccessful) {
                val imageResponse = response.body()
                if (imageResponse != null) {
                    val imageBytes = Base64.decode(imageResponse.data, Base64.DEFAULT)
                    // La decodificación del bitmap es intensiva, se hace en el dispatcher Default
                    val bitmap = withContext(Dispatchers.Default) {
                        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                    }
                    imageBitmap = bitmap
                } else {
                    hasError = true
                }
            } else {
                hasError = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            hasError = true
        }
        isLoading = false
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            hasError || imageBitmap == null -> {
                Icon(Icons.Default.BrokenImage, contentDescription = "Error loading image")
            }
            else -> {
                Image(
                    bitmap = imageBitmap!!.asImageBitmap(),
                    contentDescription = contentDescription,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = contentScale
                )
            }
        }
    }
}
