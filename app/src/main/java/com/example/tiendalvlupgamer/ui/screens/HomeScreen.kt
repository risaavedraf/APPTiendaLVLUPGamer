package com.example.tiendalvlupgamer.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendalvlupgamer.data.network.RetrofitClient
import com.example.tiendalvlupgamer.data.repository.ProductoRepository
import com.example.tiendalvlupgamer.model.ProductoResponse
import com.example.tiendalvlupgamer.ui.components.NetworkImage
import com.example.tiendalvlupgamer.ui.navigation.AppScreens
import com.example.tiendalvlupgamer.viewmodel.ProductoViewModel
import com.example.tiendalvlupgamer.viewmodel.ProductoViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: ProductoViewModel = viewModel(
        factory = ProductoViewModelFactory(
            ProductoRepository(RetrofitClient.productoApiService)
        )
    )
) {
    val context = LocalContext.current
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

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("LVLUP Gamer Store") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {                    itemsIndexed(productos) { index, producto ->
                        // Cuando el usuario se acerca al final de la lista, carga la siguiente página.
                        if (index == productos.size - 1 && !isLoadingMore) {
                            LaunchedEffect(Unit) { // Usamos LaunchedEffect para llamar una sola vez por recomposición
                                viewModel.loadNextPage()
                            }
                        }
                        ProductCard(producto = producto, onProductClick = {
                            navController.navigate(AppScreens.ProductDetailScreen.createRoute(producto.id.toString()))
                        })
                    }
                    if (isLoadingMore) {
                        item {
                           Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center){
                               CircularProgressIndicator()
                           }
                        }
                        item { // Ocupa la segunda celda de la grilla para mantener el layout
                           Spacer(Modifier.fillMaxWidth())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    producto: ProductoResponse,
    onProductClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onProductClick)
    ) {
        Column {
            NetworkImage(
                imageUrl = producto.imagenUrl,
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f), // Proporción 1:1 para la imagen
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${String.format("%,.0f", producto.price)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
