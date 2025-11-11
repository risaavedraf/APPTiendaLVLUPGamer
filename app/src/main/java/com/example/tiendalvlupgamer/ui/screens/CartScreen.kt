package com.example.tiendalvlupgamer.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendalvlupgamer.data.network.RetrofitClient
import com.example.tiendalvlupgamer.data.repository.CarritoRepository
import com.example.tiendalvlupgamer.data.repository.PedidoRepository
import com.example.tiendalvlupgamer.model.CarritoItemResponse
import com.example.tiendalvlupgamer.model.CarritoResponse
import com.example.tiendalvlupgamer.ui.components.NetworkImage
import com.example.tiendalvlupgamer.ui.navigation.AppScreens
import com.example.tiendalvlupgamer.viewmodel.CartViewModel
import com.example.tiendalvlupgamer.viewmodel.CartViewModelFactory
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: CartViewModel = viewModel(
        factory = CartViewModelFactory(
            carritoRepository = CarritoRepository(RetrofitClient.carritoApiService),
            pedidoRepository = PedidoRepository(RetrofitClient.pedidoApiService)
        )
    )

    val cartState by viewModel.cartState.observeAsState()
    val error by viewModel.error.observeAsState()
    val couponMessage by viewModel.couponMessage.observeAsState()

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }
    
    LaunchedEffect(couponMessage) {
        couponMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearCouponMessage()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Carrito de Compras") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            if (cartState == null || cartState?.items.isNullOrEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Tu carrito está vacío", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                val state = cartState!!
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(state.items, key = { it.productoId }) { item ->
                        CartItemRow(item = item, viewModel = viewModel)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                CouponSection(state, viewModel)

                PriceDetailsSection(state)

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate(AppScreens.DireccionesScreen.createRoute(selectionMode = true)) },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Continuar al Pago", style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Composable
fun CouponSection(state: CarritoResponse, viewModel: CartViewModel) {
    var couponInput by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        if (state.descuento == 0.0) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = couponInput,
                    onValueChange = { couponInput = it },
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
                Text("Cupón aplicado", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                IconButton(onClick = { viewModel.removeCoupon() }) {
                    Icon(Icons.Default.Clear, contentDescription = "Quitar cupón")
                }
            }
        }
    }
}

@Composable
fun PriceDetailsSection(state: CarritoResponse) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Subtotal", style = MaterialTheme.typography.bodyLarge)
            Text(formatCurrency(state.subtotal), style = MaterialTheme.typography.bodyLarge)
        }
        if (state.descuento > 0) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Descuento", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                Text("- ${formatCurrency(state.descuento)}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
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
fun CartItemRow(item: CarritoItemResponse, viewModel: CartViewModel) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            NetworkImage(
                imageUrl = item.imagenUrl, 
                contentDescription = item.nombreProducto, 
                modifier = Modifier.size(64.dp)
            )
            Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
                Text(item.nombreProducto, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(formatCurrency(item.precioUnitario), style = MaterialTheme.typography.bodyMedium)
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = { 
                    if (item.cantidad > 1) {
                        viewModel.updateQuantity(item.productoId, item.cantidad - 1) 
                    } else {
                        viewModel.deleteItem(item.productoId)
                    }
                }) {
                    Icon(if (item.cantidad > 1) Icons.Default.Remove else Icons.Default.Delete, contentDescription = "Restar/Eliminar")
                }
                Text(item.cantidad.toString(), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                IconButton(onClick = { viewModel.updateQuantity(item.productoId, item.cantidad + 1) }) {
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
