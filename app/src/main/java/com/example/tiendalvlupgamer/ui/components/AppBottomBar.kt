package com.example.tiendalvlupgamer.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.tiendalvlupgamer.ui.navigation.AppScreens

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen(AppScreens.HomeScreen.route, "Inicio", Icons.Default.Home)
    object Search : BottomBarScreen(AppScreens.SearchScreen.route, "Buscar", Icons.Default.Search)
    object Profile : BottomBarScreen(AppScreens.ProfileScreen.route, "Perfil", Icons.Default.Person)
    object Cart : BottomBarScreen(AppScreens.CartScreen.route, "Carrito", Icons.Default.ShoppingCart)
    object Menu : BottomBarScreen(AppScreens.MenuScreen.route, "Mas", Icons.Default.Menu) // <-- ¡CORREGIDO!
}

@Composable
fun AppBottomBar(navController: NavController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Search,
        BottomBarScreen.Cart,
        BottomBarScreen.Profile,
        BottomBarScreen.Menu
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        screens.forEach { screen ->
            NavigationBarItem(
                label = { Text(screen.title) },
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}