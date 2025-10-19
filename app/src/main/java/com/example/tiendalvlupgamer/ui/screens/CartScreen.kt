package com.example.tiendalvlupgamer.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendalvlupgamer.model.local.AppDatabase
import com.example.tiendalvlupgamer.viewmodel.CartViewModel
import com.example.tiendalvlupgamer.viewmodel.CartViewModelFactory
import com.example.tiendalvlupgamer.viewmodel.CartUiItem
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CartScreen(navController: NavController) {
    val cartDao = AppDatabase.get(LocalContext.current).cartDao()
    val viewModel: CartViewModel = viewModel(factory = CartViewModelFactory(cartDao))
    val cartState by viewModel.cartState.collectAsState(initial = Pair(emptyList(), 0.0))
    val (cartItems, totalPrice) = cartState

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Carrito de Compras", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(16.dp))

        if (cartItems.isEmpty()) {
            Text("Tu carrito está vacío", style = MaterialTheme.typography.bodyLarge)
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(cartItems) { item -> // <-- ¡AQUÍ ESTÁ LA CORRECCIÓN!
                    CartItemRow(product = item.product, quantity = item.quantity, viewModel = viewModel)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
            format.maximumFractionDigits = 0 // Sin decimales
            val formattedPrice = format.format(totalPrice)
            Text("Total: $formattedPrice", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.clearCart() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Vaciar Carrito")
            }
        }
    }
}

@Composable
fun CartItemRow(product: com.example.tiendalvlupgamer.model.local.ProductEntity, quantity: Int, viewModel: CartViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = product.image),
                contentDescription = product.name,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Fit
            )
            Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                Text(product.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
                format.maximumFractionDigits = 0 // Sin decimales
                val formattedPrice = format.format(product.price)
                Text(formattedPrice, style = MaterialTheme.typography.bodyMedium)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconButton(onClick = { viewModel.addOrUpdateProduct(product, quantity - 1) }) {
                    Text("-")
                }
                Text(quantity.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                IconButton(onClick = { viewModel.addOrUpdateProduct(product, quantity + 1) }) {
                    Text("+")
                }
            }
        }
    }
}