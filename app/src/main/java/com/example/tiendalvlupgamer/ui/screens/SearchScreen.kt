package com.example.tiendalvlupgamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.levelupgamer.ui.components.ProductCard
import com.example.levelupgamer.ui.navigation.AppScreens
import com.example.tiendalvlupgamer.model.local.AppDatabase
import com.example.tiendalvlupgamer.viewmodel.ProductViewModel
import com.example.tiendalvlupgamer.viewmodel.ProductViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {


    val productDao = AppDatabase.get(LocalContext.current).productDao()
    val reviewDao = AppDatabase.get(LocalContext.current).reviewDao()
    val viewModel: ProductViewModel = viewModel(factory = ProductViewModelFactory(productDao,reviewDao))


    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()


    Column(
        modifier = Modifier.background(MaterialTheme.colorScheme.background)
    ) {  TopAppBar(
        title = {
            // 2. Campo de texto para la búsqueda
            TextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 15.dp),

                placeholder = { Text("Buscar productos...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Icono de búsqueda"
                    )
                },
                singleLine = true
            )
        }
    )

        // --- Lista de Resultados ---
        LazyColumn(
            modifier = Modifier.padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (searchQuery.isNotBlank() && searchResults.isEmpty()) {
                item {
                    Text(
                        text = "No se encontraron productos para \"$searchQuery\"",
                        modifier = Modifier.padding(vertical = 24.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(searchResults) { product ->
                    ProductCard(
                        product = product,
                        onClick = {

                            navController.navigate(
                                AppScreens.ProductDetailScreen.createRoute(
                                    product.id
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}