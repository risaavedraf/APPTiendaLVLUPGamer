package com.example.tiendalvlupgamer.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendalvlupgamer.model.local.AppDatabase
import com.example.tiendalvlupgamer.model.local.ProductEntity
import com.example.tiendalvlupgamer.viewmodel.CartState
import com.example.tiendalvlupgamer.viewmodel.CartViewModel
import com.example.tiendalvlupgamer.viewmodel.CartViewModelFactory
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CartScreen(navController: NavController) {
    val cartDao = AppDatabase.get(LocalContext.current).cartDao()
    val viewModel: CartViewModel = viewModel(factory = CartViewModelFactory(cartDao))
    val state by viewModel.cartState.collectAsState(initial = CartState())
    var couponInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // <-- ¡AQUÍ ESTÁ LA CORRECCIÓN!
            .padding(16.dp)
    ) {
        Text(
            "Carrito de Compras",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (state.cartItems.isEmpty()) {
            Text("Tu carrito está vacío", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 32.dp))
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(state.cartItems) { item ->
                    CartItemRow(product = item.product, quantity = item.quantity, viewModel = viewModel)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            CouponSection(state, couponInput, onCouponInputChange = { couponInput = it }, viewModel)
            
            PriceDetailsSection(state)
            
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
fun CouponSection(state: CartState, couponInput: String, onCouponInputChange: (String) -> Unit, viewModel: CartViewModel) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        if (state.appliedCoupon == null) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = couponInput,
                    onValueChange = onCouponInputChange,
                    label = { Text("Código de descuento") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        viewModel.applyCoupon(couponInput)
                        keyboardController?.hide()
                    })
                )
                Button(onClick = { 
                    viewModel.applyCoupon(couponInput)
                    keyboardController?.hide()
                 }, modifier = Modifier.padding(start = 8.dp)) {
                    Text("Aplicar")
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Cupón aplicado: ${state.appliedCoupon}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                IconButton(onClick = { viewModel.removeCoupon() }) {
                    Icon(Icons.Default.Clear, contentDescription = "Quitar cupón")
                }
            }
        }

        state.couponMessage?.let {
            val messageColor = if (it.contains("éxito")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            Text(it, color = messageColor, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
fun PriceDetailsSection(state: CartState) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Subtotal", style = MaterialTheme.typography.bodyLarge)
            Text(formatCurrency(state.subtotal), style = MaterialTheme.typography.bodyLarge)
        }
        if (state.discountAmount > 0) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Descuento", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                Text("- ${formatCurrency(state.discountAmount)}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Total", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(formatCurrency(state.total), style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CartItemRow(product: ProductEntity, quantity: Int, viewModel: CartViewModel) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = product.image), contentDescription = product.name, modifier = Modifier.size(64.dp), contentScale = ContentScale.Fit)
            Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                Text(product.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(formatCurrency(product.price.toDouble()), style = MaterialTheme.typography.bodyMedium)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = { viewModel.addOrUpdateProduct(product, quantity - 1) }) {
                    Icon(Icons.Default.Remove, contentDescription = "Restar")
                }
                Text(quantity.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                IconButton(onClick = { viewModel.addOrUpdateProduct(product, quantity + 1) }) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir")
                }
            }
        }
    }
}

@Composable
private fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    format.maximumFractionDigits = 0
    return format.format(amount)
}