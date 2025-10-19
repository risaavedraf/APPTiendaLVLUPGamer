package com.example.tiendalvlupgamer.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil.compose.rememberAsyncImagePainter
import com.example.levelupgamer.ui.navigation.AppScreens
import com.example.tiendalvlupgamer.data.entity.User
import com.example.tiendalvlupgamer.util.SessionManager

@Composable
fun ProfileScreen(navController: NavController) {
    val user by SessionManager.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // El contenido de la pantalla cambia en función de si el usuario es nulo o no.
        if (user == null) {
            GuestProfileView() // Vista para invitados
        } else {
            UserProfileView(user!!, onLogout = {
                SessionManager.logout()
                // Navegar de vuelta a la WelcomeScreen y limpiar el historial.
                navController.navigate(AppScreens.WelcomeScreen.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                }
            }) // Vista para usuarios con sesión iniciada
        }
    }
}

@Composable
private fun GuestProfileView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Perfil de invitado",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Usuario Invitado",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Inicia sesión para ver tu perfil y más.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun UserProfileView(user: User, onLogout: () -> Unit) {
    val yellow = Color(0xFFFFC400)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // --- FOTO DE PERFIL (CORREGIDA) ---
        // Como la entidad User no tiene imagen, mostramos siempre el icono genérico.
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Foto de perfil",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, yellow, CircleShape),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- NOMBRE COMPLETO ---
        Text(
            text = "${user.name} ${user.lastName}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        // --- USERNAME ---
        Text(
            text = "@${user.username}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))
        Divider()

        // --- INFORMACIÓN DETALLADA ---
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InfoRow(label = "Correo Electrónico", value = user.email)
            InfoRow(label = "Fecha de Nacimiento", value = user.birthDate ?: "No especificada")
        }

        Divider()
        Spacer(modifier = Modifier.weight(1f)) // Empuja el botón de logout hacia abajo

        // --- BOTÓN DE CERRAR SESIÓN ---
        Button(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Cerrar Sesión")
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}