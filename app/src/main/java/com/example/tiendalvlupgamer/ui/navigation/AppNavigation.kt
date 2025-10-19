package com.example.tiendalvlupgamer.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.levelupgamer.ui.components.AppBottomBar
import com.example.levelupgamer.ui.navigation.AppScreens
import com.example.tiendalvlupgamer.view.LoginScreen
import com.example.tiendalvlupgamer.view.RegisterScreen
import com.example.tiendalvlupgamer.view.WelcomeScreen
import com.example.tiendalvlupgamer.ui.screens.HomeScreen
import com.example.tiendalvlupgamer.ui.screens.ProductDetailScreen
import com.example.tiendalvlupgamer.ui.screens.CartScreen
import com.example.tiendalvlupgamer.ui.screens.ProfileScreen
import com.example.tiendalvlupgamer.ui.screens.SearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarRoutes = setOf(
        AppScreens.HomeScreen.route,
        AppScreens.SearchScreen.route,
        AppScreens.CartScreen.route,
        AppScreens.ProfileScreen.route
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                AppBottomBar(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AppScreens.WelcomeScreen.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            // --- FLUJO DE AUTENTICACIÓN ---
            composable(AppScreens.WelcomeScreen.route) {
                WelcomeScreen(
                    onNavigateRegister = { navController.navigate(AppScreens.RegisterScreen.route) },
                    onNavigateLogin = { navController.navigate(AppScreens.LoginScreen.route) },
                    onNavigateGuest = {
                        navController.navigate(AppScreens.HomeScreen.route) {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        }
                    }
                )
            }
            composable(AppScreens.LoginScreen.route) {
                LoginScreen(
                    navController = navController,
                    onLogin = { 
                        navController.navigate(AppScreens.HomeScreen.route) {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        }
                    }
                )
            }
            composable(AppScreens.RegisterScreen.route) {
                RegisterScreen(navController = navController)
            }

            // --- FLUJO PRINCIPAL DE LA APP ---
            composable(AppScreens.HomeScreen.route) {
                HomeScreen(navController)
            }
            composable(AppScreens.SearchScreen.route) {
                SearchScreen(navController)
            }
            composable(AppScreens.CartScreen.route) {
                CartScreen(navController)
            }
            composable(AppScreens.ProfileScreen.route) {
                ProfileScreen(navController)
            }
            composable(
                route = AppScreens.ProductDetailScreen.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId")
                requireNotNull(productId)
                ProductDetailScreen(navController = navController, productId = productId)
            }
        }
    }
}