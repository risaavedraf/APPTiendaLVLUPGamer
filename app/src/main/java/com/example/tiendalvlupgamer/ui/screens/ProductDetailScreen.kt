package com.example.tiendalvlupgamer.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendalvlupgamer.data.network.RetrofitClient
import com.example.tiendalvlupgamer.data.repository.ProductoRepository
import com.example.tiendalvlupgamer.ui.components.NetworkImage
import com.example.tiendalvlupgamer.viewmodel.ProductoViewModel
import com.example.tiendalvlupgamer.viewmodel.ProductoViewModelFactory
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(navController: NavController, productId: String) {
    val context = LocalContext.current

    // 1. Inyectar el ViewModel correcto que usa el Repository de Red
    val viewModel: ProductoViewModel = viewModel(
        factory = ProductoViewModelFactory(ProductoRepository(RetrofitClient.productoApiService))
    )

    // 2. Obtener datos de la red
    LaunchedEffect(productId) {
        productId.toLongOrNull()?.let {
            viewModel.getProductoById(it)
        }
    }

    val product by viewModel.productoDetail.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)

    var quantity by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text(product?.nombre ?: "Detalle del Producto") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            }
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (product == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Producto no encontrado")
            }
        } else {
            val currentProduct = product!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // 4. Adaptar la UI para usar NetworkImage
                NetworkImage(
                    imageUrl = currentProduct.imagenUrl,
                    contentDescription = currentProduct.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = currentProduct.nombre,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
                format.maximumFractionDigits = 0
                val formattedPrice = format.format(currentProduct.price)
                Text(
                    text = formattedPrice,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                // --- Lógica de Carrito y Cantidad (Preservada por ahora) ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { if (quantity > 1) quantity-- },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                        ) { Icon(Icons.Default.Remove, contentDescription = "Restar cantidad") }
                        
                        Text(
                            text = "$quantity",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        
                        IconButton(
                            onClick = { quantity++ },
                            modifier = Modifier.background(MaterialTheme.colorScheme.primary, CircleShape),
                            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary)
                        ) { Icon(Icons.Default.Add, contentDescription = "Añadir cantidad") }
                    }
                    Button(
                        onClick = {
                            // TODO: Refactorizar la lógica del carrito para que funcione con el nuevo modelo
                            Toast.makeText(context, "Añadir al carrito (lógica pendiente)", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                        Spacer(Modifier.width(8.dp))
                        Text("Añadir")
                    }
                }
                // --- FIN Lógica de Carrito ---

                Spacer(modifier = Modifier.height(24.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = currentProduct.descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Justify
                )
                
                Spacer(modifier = Modifier.height(24.dp))

                // --- Lógica de Reseñas (Preservada por ahora) ---
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Reseñas",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "La funcionalidad de reseñas se implementará en una futura versión.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                // TODO: Refactorizar la lógica de reseñas para que funcione con el nuevo modelo.
            }
        }
    }
}
