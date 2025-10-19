package com.example.tiendalvlupgamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Room
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.levelupgamer.ui.navigation.AppScreens
import com.example.tiendalvlupgamer.util.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(navController: NavController) {
    val user by SessionManager.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = { Text("Menú", fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground
            )
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            item { MenuHeader(title = "Comunidad") }
            item {
                MenuRow(
                    icon = Icons.Filled.EmojiEvents,
                    title = "Eventos",
                    onClick = { navController.navigate(AppScreens.EventsScreen.route) }
                )
            }

            item { MenuHeader(title = "Mi Cuenta") }
            item { MenuRow(icon = Icons.AutoMirrored.Filled.ListAlt, title = "Mis Pedidos") { /* TODO */ } }
            item { MenuRow(icon = Icons.Filled.Room, title = "Direcciones de Envío") { /* TODO */ } }
            item { MenuRow(icon = Icons.Filled.Payment, title = "Métodos de Pago") { /* TODO */ } }

            item { MenuHeader(title = "Ajustes de la App") }
            item { MenuRow(icon = Icons.Filled.Notifications, title = "Notificaciones") { /* TODO */ } }
            item { MenuRow(icon = Icons.Filled.Style, title = "Tema") { /* TODO */ } }

            item { MenuHeader(title = "Soporte e Información") }
            item { MenuRow(icon = Icons.AutoMirrored.Filled.HelpOutline, title = "Centro de Ayuda") { /* TODO */ } }
            item { MenuRow(icon = Icons.Filled.Info, title = "Sobre Level-Up Gamer") { /* TODO */ } }
            item { MenuRow(icon = Icons.Filled.Policy, title = "Términos y Condiciones") { /* TODO */ } }
        }

        val (buttonText, buttonColor, onButtonClick) = if (user == null) {
            Triple("Iniciar Sesión / Registrarse", MaterialTheme.colorScheme.primary) {
                navController.navigate(AppScreens.WelcomeScreen.route) {
                    popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                }
            }
        } else {
            Triple("Cerrar Sesión", MaterialTheme.colorScheme.error) {
                SessionManager.logout()
                navController.navigate(AppScreens.WelcomeScreen.route) {
                    popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                }
            }
        }

        Button(
            onClick = onButtonClick,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
        ) {
            Text(buttonText)
        }
    }
}

@Composable
private fun MenuHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
private fun MenuRow(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
    }
}