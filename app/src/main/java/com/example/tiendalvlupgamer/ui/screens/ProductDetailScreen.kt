import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendalvlupgamer.model.local.AppDatabase
import com.example.tiendalvlupgamer.model.local.ReviewEntity
import com.example.tiendalvlupgamer.viewmodel.ProductViewModel
import com.example.tiendalvlupgamer.viewmodel.ProductViewModelFactory
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(navController: NavController, productId: String) {
    val context = LocalContext.current
    // Obtenemos ambos DAOs
    val db = AppDatabase.get(context)
    val productDao = db.productDao()
    val reviewDao = db.reviewDao()

    // Usamos la Factory actualizada
    val viewModel: ProductViewModel =
        viewModel(factory = ProductViewModelFactory(productDao, reviewDao))

    LaunchedEffect(productId) {
        viewModel.getProductById(productId)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearSelectedProduct()
        }
    }

    val product by viewModel.selectedProduct.collectAsState()
    var quantity by remember { mutableStateOf(1) }

    // Estado para la nueva reseña
    val reviews by viewModel.reviewsForProduct.collectAsState()
    var userRating by remember { mutableStateOf(0) }
    var userComment by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        TopAppBar(
            title = { Text(product?.name ?: "Detalle del Producto") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            }
        )

        if (product == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // --- SECCIÓN DE DETALLES DEL PRODUCTO (SIN CAMBIOS) ---
                val painter: Painter = painterResource(id = product!!.image)
                val aspectRatio = painter.intrinsicSize.width / painter.intrinsicSize.height
                Image(
                    painter = painter,
                    contentDescription = product!!.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(aspectRatio)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentScale = ContentScale.FillBounds
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = product!!.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
                format.maximumFractionDigits = 0
                val formattedPrice = format.format(product!!.price)
                Text(
                    text = formattedPrice,
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
                            modifier = Modifier.background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                CircleShape
                            ),
                        ) { Icon(Icons.Default.Remove, contentDescription = "Restar cantidad") }
                        Text(
                            text = "$quantity",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        IconButton(
                            onClick = { quantity++ },
                            modifier = Modifier.background(
                                MaterialTheme.colorScheme.primary,
                                CircleShape
                            ),
                            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.onPrimary)
                        ) { Icon(Icons.Default.Add, contentDescription = "Añadir cantidad") }
                    }
                    Button(
                        onClick = {
                            Toast.makeText(
                                context,
                                "$quantity ${product!!.name}(s) agregado(s) al carrito",
                                Toast.LENGTH_SHORT
                            ).show()
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
                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = product!!.description,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = Modifier.height(24.dp))
                // --- FIN DE SECCIÓN DE DETALLES ---


                // --- INICIO DE LA NUEVA SECCIÓN DE RESEÑAS ---
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Reseñas",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Campo para dejar una reseña
                OutlinedTextField(
                    value = userComment,
                    onValueChange = { userComment = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Escribe tu reseña...") },
                    maxLines = 4
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Selector de estrellas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StarRatingInput(rating = userRating, onRatingChange = { userRating = it })
                    Button(
                        onClick = {
                            if (userRating > 0 && userComment.isNotBlank()) {
                                viewModel.addReview(productId, userRating, userComment)
                                // Limpiar campos después de enviar
                                userComment = ""
                                userRating = 0
                                Toast.makeText(
                                    context,
                                    "¡Gracias por tu reseña!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Por favor, añade una calificación y un comentario.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        enabled = userComment.isNotBlank() && userRating > 0 // El botón solo se activa si hay reseña y rating
                    ) {
                        Text("Enviar")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Lista de reseñas existentes
                if (reviews.isEmpty()) {
                    Text(
                        text = "Todavía no hay reseñas para este producto. ¡Sé el primero!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    reviews.forEach { review ->
                        ReviewItem(review = review)
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// --- COMPOSABLES AUXILIARES PARA LAS RESEÑAS ---
@Composable
fun StarRatingInput(
    maxStars: Int = 5,
    rating: Int,
    onRatingChange: (Int) -> Unit
) {
    Row {
        for (i in 1..maxStars) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Filled.StarOutline,
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
fun ReviewItem(review: ReviewEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = review.author,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Muestra las estrellas de la reseña
                StarRatingDisplay(rating = review.rating)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = review.comment,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun StarRatingDisplay(maxStars: Int = 5, rating: Int) {
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