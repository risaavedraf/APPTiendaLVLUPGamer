package com.example.tiendalvlupgamer.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale

private const val MIN_BASE64_LENGTH = 100

/**
 * Renderiza una imagen a partir de un string Base64.
 * Se encarga de la decodificación y de mostrar un placeholder si la imagen es inválida.
 * La clave es el Box exterior, que asegura que siempre haya un contenedor con tamaño.
 */
@Composable
fun Base64Image(
    base64String: String?,
    modifier: Modifier = Modifier,
    contentDescription: String?,
    contentScale: ContentScale = ContentScale.Crop,
    placeholder: @Composable (() -> Unit)? = null
) {
    val bitmap = remember(base64String) {
        decodeAndValidateImage(base64String)
    }

    // Este Box recibe el modifier con el tamaño, solucionando el problema de renderizado.
    Box(modifier = modifier) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(), // La imagen llena el Box padre.
                contentScale = contentScale
            )
        } else {
            // El placeholder se renderiza dentro del Box, heredando su tamaño.
            placeholder?.invoke()
        }
    }
}

private fun decodeAndValidateImage(base64String: String?): Bitmap? {
    if (base64String.isNullOrBlank() || base64String.length < MIN_BASE64_LENGTH) {
        return null
    }

    val base64Image = if (base64String.contains("base64:")) {
        base64String.substringAfter("base64:")
    } else {
        base64String
    }

    val bitmap = try {
        val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    } catch (e: IllegalArgumentException) {
        return null
    }

    if (bitmap != null && (bitmap.width <= 1 || bitmap.height <= 1)) {
        return null
    }

    return bitmap
}
