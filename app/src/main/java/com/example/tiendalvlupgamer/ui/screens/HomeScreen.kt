package com.example.tiendalvlupgamer.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendalvlupgamer.data.network.RetrofitClient
import com.example.tiendalvlupgamer.data.repository.ProductoRepository
import com.example.tiendalvlupgamer.model.CategoriaConProductos
import com.example.tiendalvlupgamer.ui.components.ProductCard
import com.example.tiendalvlupgamer.ui.navigation.AppScreens
import com.example.tiendalvlupgamer.viewmodel.HomeViewModel
import com.example.tiendalvlupgamer.viewmodel.HomeViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(
            ProductoRepository(RetrofitClient.productoApiService)
        )
    )
) {
    val context = LocalContext.current
    val homeState by viewModel.homeState.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
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
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(homeState) { categoriaConProductos ->
                        if (categoriaConProductos.productos.isNotEmpty()) {
                            ProductCarousel(navController, categoriaConProductos)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCarousel(
    navController: NavController,
    data: CategoriaConProductos
) {
    Column {
        Text(
            text = data.categoria.nombre,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(data.productos) { producto ->
                ProductCard(
                    modifier = Modifier.width(160.dp), // Damos un ancho fijo a las tarjetas
                    product = producto,
                    onClick = {
                        navController.navigate(AppScreens.ProductDetailScreen.createRoute(producto.id.toString()))
                    }
                )
            }
        }
    }
}
