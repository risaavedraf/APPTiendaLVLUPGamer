package com.example.tiendalvlupgamer.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendalvlupgamer.data.network.RetrofitClient
import com.example.tiendalvlupgamer.data.repository.ProductoRepository
import com.example.tiendalvlupgamer.ui.components.ProductCard
import com.example.tiendalvlupgamer.ui.navigation.AppScreens
import com.example.tiendalvlupgamer.viewmodel.ProductoViewModel
import com.example.tiendalvlupgamer.viewmodel.ProductoViewModelFactory
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    // 1. Conectar el ViewModel de Red
    val viewModel: ProductoViewModel = viewModel(
        key = "search_vm", // Usamos una key para tener una instancia separada de la de HomeScreen
        factory = ProductoViewModelFactory(ProductoRepository(RetrofitClient.productoApiService))
    )

    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    val productos by viewModel.productos.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val isLoadingMore by viewModel.isLoadingMore.observeAsState(false)
    val error by viewModel.error.observeAsState()

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.clearError()
        }
    }

    // 2. Lógica de Búsqueda con "debounce"
    LaunchedEffect(searchQuery) {
        // Espera 500ms después de la última pulsación antes de buscar
        delay(500)
        if (searchQuery.isNotBlank()) {
            viewModel.search(searchQuery)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Buscar productos...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Icono de búsqueda")
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                        unfocusedIndicatorColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        )

        // 3. Mostrar Resultados Paginados
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (productos.isEmpty() && searchQuery.isNotBlank()) {
                Text("No se encontraron resultados", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(productos) { index, producto ->
                        if (index == productos.size - 1 && !isLoadingMore) {
                            LaunchedEffect(Unit) { viewModel.loadNextPage() }
                        }
                        ProductCard(product = producto, onClick = {
                            navController.navigate(AppScreens.ProductDetailScreen.createRoute(producto.id.toString()))
                        })
                    }
                    if (isLoadingMore) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}