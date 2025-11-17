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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Star
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
import com.example.tiendalvlupgamer.data.repository.CarritoRepository
import com.example.tiendalvlupgamer.data.repository.PedidoRepository
import com.example.tiendalvlupgamer.data.repository.ProductoRepository
import com.example.tiendalvlupgamer.data.repository.ReviewRepository
import com.example.tiendalvlupgamer.model.ReviewResponse
import com.example.tiendalvlupgamer.ui.components.UrlBase64Image
import com.example.tiendalvlupgamer.ui.navigation.AppScreens
import com.example.tiendalvlupgamer.util.SessionManager
import com.example.tiendalvlupgamer.viewmodel.CartViewModel
import com.example.tiendalvlupgamer.viewmodel.CartViewModelFactory
import com.example.tiendalvlupgamer.viewmodel.ProductoViewModel
import com.example.tiendalvlupgamer.viewmodel.ProductoViewModelFactory
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(navController: NavController, productId: Long) {
    val context = LocalContext.current

    val productoViewModel: ProductoViewModel = viewModel(
        key = "product_detail_vm",
        factory = ProductoViewModelFactory(
            productoRepository = ProductoRepository(RetrofitClient.productoApiService),
            reviewRepository = ReviewRepository(RetrofitClient.reviewApiService)
        )
    )
    
    val cartViewModel: CartViewModel = viewModel(
        factory = CartViewModelFactory(
            carritoRepository = CarritoRepository(RetrofitClient.carritoApiService),
            pedidoRepository = PedidoRepository(RetrofitClient.pedidoApiService)
        )
    )
    
    val user by SessionManager.currentUser.collectAsState(null)

    LaunchedEffect(productId) {
        productoViewModel.getProductoById(productId)
    }

    val product by productoViewModel.productoDetail.observeAsState()
    val reviews by productoViewModel.reviews.observeAsState(emptyList())
    val isLoading by productoViewModel.isLoading.observeAsState(false)
    val reviewAdded by productoViewModel.reviewAdded.observeAsState(false)
    val error by productoViewModel.error.observeAsState()

    var quantity by remember { mutableStateOf(1) }
    var userRating by remember { mutableStateOf(0) }
    var userComment by remember { mutableStateOf("") }
    
    LaunchedEffect(reviewAdded) {
        if (reviewAdded) {
            Toast.makeText(context, "¡Gracias por tu reseña!", Toast.LENGTH_SHORT).show()
            userComment = ""
            userRating = 0
            productoViewModel.onReviewAddedConsumed()
        }
    }

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            productoViewModel.clearError()
        }
    }

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

        if (isLoading && product == null) {
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
                UrlBase64Image(
                    imageUrl = currentProduct.imagenUrl,
                    contentDescription = currentProduct.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp)),
                    placeholder = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = currentProduct.nombre, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
                format.maximumFractionDigits = 0
                Text(
                    text = format.format(currentProduct.price),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))
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
                            cartViewModel.addItem(currentProduct.id, quantity)
                            Toast.makeText(context, "$quantity ${currentProduct.nombre}(s) agregado(s)", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                        Spacer(Modifier.width(8.dp))
                        Text("Añadir")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Descripción", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = currentProduct.descripcion, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Justify)

                Spacer(modifier = Modifier.height(24.dp))

                // --- SECCIÓN DE RESEÑAS --- 
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Reseñas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))

                if (user != null) {
                    OutlinedTextField(
                        value = userComment,
                        onValueChange = { userComment = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Escribe tu reseña...") },
                        maxLines = 4
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StarRatingInput(rating = userRating, onRatingChange = { userRating = it })
                        Button(
                            onClick = {
                                productoViewModel.addReview(currentProduct.id, userRating, userComment)
                            },
                            enabled = userComment.isNotBlank() && userRating > 0
                        ) {
                            Text("Enviar")
                        }
                    }
                } else {
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { navController.navigate(AppScreens.LoginScreen.route) }
                    ) {
                        Text("Inicia sesión para dejar una reseña")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (isLoading && reviews.isEmpty()) {
                    CircularProgressIndicator()
                } else if (reviews.isEmpty()) {
                    Text("Todavía no hay reseñas para este producto. ¡Sé el primero!")
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        reviews.forEach { review ->
                            ReviewItem(review = review)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StarRatingInput(maxStars: Int = 5, rating: Int, onRatingChange: (Int) -> Unit) {
    Row {
        for (i in 1..maxStars) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = "Estrella $i",
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onRatingChange(i) },
                tint = if (i <= rating) Color(0xFFFFD700) else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ReviewItem(review: ReviewResponse) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = review.author,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                StarRatingDisplay(rating = review.calificacion)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = review.comentario, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun StarRatingDisplay(maxStars: Int = 5, rating: Int) {
    Row {
        for (i in 1..maxStars) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = if (i <= rating) Color(0xFFFFD700) else Color.Gray
            )
        }
    }
}
