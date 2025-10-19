package com.example.tiendalvlupgamer.ui.navigation

import HomeScreen
import ProductDetailScreen
import SearchScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tiendalvlupgamer.R
import com.example.levelupgamer.ui.components.AppBottomBar
import com.example.levelupgamer.ui.navigation.AppScreens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // 1. EL SCAFFOLD PRINCIPAL AHORA VIVE AQUÍ
    Scaffold(
        containerColor = MaterialTheme.colorScheme.primary,
        bottomBar = { AppBottomBar(navController = navController) } // <-- La barra se muestra en todas las pantallas principales
    ) { paddingValues ->
        // 2. EL NAVHOST USA EL PADDING DEL SCAFFOLD
        NavHost(
            navController = navController,
            startDestination = AppScreens.HomeScreen.route,
            modifier = Modifier.padding(paddingValues) // <-- MUY IMPORTANTE
        ) {
            composable(AppScreens.HomeScreen.route) {
                HomeScreen(navController) // Le pasas el navController si necesita navegar a detalle
            }

            // --- AÑADE EL COMPOSABLE PARA LA PANTALLA DE BÚSQUEDA ---
            composable(AppScreens.SearchScreen.route) {
                SearchScreen(navController)
            }

            composable(
                route = AppScreens.ProductDetailScreen.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")
                requireNotNull(productId)
                ProductDetailScreen(navController = navController,productId = productId)
            }
        }
    }
}