package com.example.tiendalvlupgamer.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.tiendalvlupgamer.model.ProductoResponse
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductCard(
    modifier: Modifier = Modifier,
    product: ProductoResponse, // 1. Cambiado de ProductEntity a ProductoResponse
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column {
            // 2. Reemplazado Image con NetworkImage
            NetworkImage(
                imageUrl = product.imagenUrl,
                contentDescription = "Imagen de ${product.nombre}",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // Proporción 1:1 para un look uniforme
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = product.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2, // Limita el nombre a 2 líneas
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.categoria.nombre, // 3. Ajustado para usar el objeto CategoriaResponse
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
                format.maximumFractionDigits = 0
                val formattedPrice = format.format(product.price)

                Text(
                    text = formattedPrice,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
