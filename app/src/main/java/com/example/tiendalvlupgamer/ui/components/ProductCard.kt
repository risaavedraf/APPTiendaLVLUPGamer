// En: ui/components/ProductCard.kt

package com.example.levelupgamer.ui.components

import android.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextOverflow
import com.example.tiendalvlupgamer.model.local.ProductEntity
import java.nio.file.WatchEvent
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ProductCard( modifier: Modifier = Modifier,product: ProductEntity, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = MaterialTheme.shapes.medium



    ) {
        // Usamos una Columna para apilar la imagen y el texto verticalmente.
        Column {
            // --- IMAGEN DEL PRODUCTO ---


            Image(
                painter = painterResource(id = product.image),
                contentDescription = "Imagen de ${product.name}",
                // La imagen ocupará todo el ancho de la tarjeta y tendrá una altura fija.
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.White),

                // Esto asegura que la imagen llene el espacio sin distorsionarse.
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center


            )

            // Añadimos un padding aquí para separar el texto de los bordes de la tarjeta.
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleLarge,

                )

                Spacer(modifier = Modifier.height(4.dp)) // Pequeño espacio extra

                Text(
                    text = product.category,
                    style = MaterialTheme.typography.bodyMedium,
                     // Un toque de color
                )

                Spacer(modifier = Modifier.height(8.dp)) // Espacio antes del precio
                val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
                format.maximumFractionDigits = 0 // Sin decimales
                val formattedPrice = format.format(product!!.price)

                Text(
                    // El tipo de 'price' ya es String en tu modelo, no necesitamos el "$".
                    text = formattedPrice,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,

                )
            }
        }
    }
}