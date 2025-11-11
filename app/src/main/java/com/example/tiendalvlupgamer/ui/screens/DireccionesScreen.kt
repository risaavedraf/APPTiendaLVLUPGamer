package com.example.tiendalvlupgamer.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendalvlupgamer.data.network.RetrofitClient
import com.example.tiendalvlupgamer.data.repository.CarritoRepository
import com.example.tiendalvlupgamer.data.repository.DireccionRepository
import com.example.tiendalvlupgamer.data.repository.PedidoRepository
import com.example.tiendalvlupgamer.model.DireccionResponse
import com.example.tiendalvlupgamer.ui.navigation.AppScreens
import com.example.tiendalvlupgamer.viewmodel.CartViewModel
import com.example.tiendalvlupgamer.viewmodel.CartViewModelFactory
import com.example.tiendalvlupgamer.viewmodel.DireccionViewModel
import com.example.tiendalvlupgamer.viewmodel.DireccionViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DireccionesScreen(
    navController: NavController,
    selectionMode: Boolean,
    direccionViewModel: DireccionViewModel = viewModel(
        factory = DireccionViewModelFactory(DireccionRepository(RetrofitClient.direccionApiService))
    ),
    cartViewModel: CartViewModel = viewModel(
        factory = CartViewModelFactory(
            carritoRepository = CarritoRepository(RetrofitClient.carritoApiService),
            pedidoRepository = PedidoRepository(RetrofitClient.pedidoApiService)
        )
    )
) {
    val context = LocalContext.current
    val direcciones by direccionViewModel.direcciones.observeAsState(emptyList())
    val isLoading by direccionViewModel.isLoading.observeAsState(false)
    
    val checkoutResult by cartViewModel.checkoutResult.observeAsState()
    val checkoutError by cartViewModel.error.observeAsState()

    LaunchedEffect(Unit) {
        direccionViewModel.loadDirecciones()
    }

    LaunchedEffect(checkoutResult) {
        checkoutResult?.let {
            Toast.makeText(context, "¡Pedido realizado con éxito!", Toast.LENGTH_LONG).show()
            navController.navigate(AppScreens.PedidosScreen.route) {
                popUpTo(AppScreens.HomeScreen.route)
            }
            cartViewModel.onCheckoutConsumed()
        }
    }

    LaunchedEffect(checkoutError) {
        checkoutError?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            cartViewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (selectionMode) "Seleccionar Dirección" else "Gestionar Direcciones") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            if (!selectionMode) { 
                FloatingActionButton(onClick = { navController.navigate(AppScreens.DireccionFormScreen.createRoute(0L)) }) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir Dirección")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            if (isLoading && direcciones.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (direcciones.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("No tienes direcciones guardadas.")
                    if (!selectionMode) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { navController.navigate(AppScreens.DireccionFormScreen.createRoute(0L)) }) {
                            Text("Añadir una dirección")
                        }
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(direcciones, key = { it.id }) { direccion ->
                        DireccionItem(
                            navController = navController,
                            direccion = direccion,
                            selectionMode = selectionMode,
                            onSelect = { cartViewModel.realizarCheckout(direccion.id) }, 
                            onDelete = { direccionViewModel.deleteDireccion(direccion.id) } 
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DireccionItem(
    navController: NavController,
    direccion: DireccionResponse,
    selectionMode: Boolean,
    onSelect: (DireccionResponse) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = selectionMode) { onSelect(direccion) }
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = direccion.nombre, fontWeight = FontWeight.Bold)
                Text(text = "${direccion.calle} ${direccion.numeroCasa}")
                if (!direccion.numeroDepartamento.isNullOrEmpty()) {
                    Text(text = "Depto: ${direccion.numeroDepartamento}")
                }
                Text(text = "${direccion.comuna}, ${direccion.ciudad}")
            }
            if (!selectionMode) {
                Row {
                    IconButton(onClick = { navController.navigate(AppScreens.DireccionFormScreen.createRoute(direccion.id)) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}
