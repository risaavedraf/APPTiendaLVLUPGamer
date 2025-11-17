package com.example.tiendalvlupgamer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.tiendalvlupgamer.data.network.RetrofitClient
import com.example.tiendalvlupgamer.data.repository.ImagenRepository
import com.example.tiendalvlupgamer.data.repository.ProfileRepository
import com.example.tiendalvlupgamer.model.DireccionResponse
import com.example.tiendalvlupgamer.model.FullProfileResponse
import com.example.tiendalvlupgamer.model.ReviewResponse
import com.example.tiendalvlupgamer.ui.components.Base64Image
import com.example.tiendalvlupgamer.ui.components.InfoRow
import com.example.tiendalvlupgamer.ui.navigation.AppScreens
import com.example.tiendalvlupgamer.util.SessionManager
import com.example.tiendalvlupgamer.viewmodel.ProfileViewModel
import com.example.tiendalvlupgamer.viewmodel.ProfileViewModelFactory
import java.time.format.DateTimeFormatter

@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(
            ProfileRepository(RetrofitClient.profileApiService),
            ImagenRepository(RetrofitClient.imagenApiService)
        )
    )
) {
    val user by SessionManager.currentUser.collectAsState()
    val fullProfile by profileViewModel.profile.observeAsState()

    LaunchedEffect(navController.currentBackStackEntry) {
        if (user != null) {
            profileViewModel.getMyProfile()
        }
    }

    if (user == null) {
        GuestProfileView()
    } else {
        val profile = fullProfile
        if (profile != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    ProfileHeader(profile, onEditProfile = {
                        navController.navigate(AppScreens.EditProfileScreen.route)
                    })
                    Spacer(modifier = Modifier.height(24.dp))
                    Divider()
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        InfoRow(label = "Correo Electrónico", value = profile.email)
                        InfoRow(
                            label = "Fecha de Nacimiento",
                            value = profile.birthDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "No especificada"
                        )
                        TextButton(onClick = { navController.navigate(AppScreens.PedidosScreen.route) }) {
                            Text("Mis Pedidos")
                        }
                    }
                    Divider()
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Mis Direcciones", style = MaterialTheme.typography.titleMedium)
                        TextButton(onClick = { navController.navigate(AppScreens.DireccionesScreen.createRoute()) }) {
                            Text("Gestionar")
                        }
                    }
                }

                if (profile.direcciones.isNotEmpty()) {
                    items(profile.direcciones) { direccion ->
                        DireccionItem(direccion)
                    }
                } else {
                    item {
                        Text("No tienes direcciones guardadas.", modifier = Modifier.padding(vertical = 8.dp))
                    }
                }

                if (profile.reviews.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Mis Reseñas", style = MaterialTheme.typography.titleMedium)
                    }
                    items(profile.reviews) { review ->
                        ReviewItem(review)
                    }
                }

                item {
                    Divider(modifier = Modifier.padding(top = 8.dp))
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            SessionManager.logout()
                            navController.navigate(AppScreens.WelcomeScreen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Cerrar Sesión")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun ProfileHeader(profile: FullProfileResponse, onEditProfile: () -> Unit) {
    val yellow = Color(0xFFFFC400)
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Base64Image(
                base64String = profile.profileImageBase64,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(2.dp, yellow, CircleShape),
                placeholder = { DefaultProfileIcon(yellow) }
            )

            IconButton(onClick = onEditProfile, modifier = Modifier.offset(x = (-8).dp, y = 8.dp)) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar perfil")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "${profile.name} ${profile.lastName}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "@${profile.username}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun DefaultProfileIcon(borderColor: Color) {
    Icon(
        imageVector = Icons.Default.Person,
        contentDescription = "Foto de perfil por defecto",
        modifier = Modifier.size(120.dp).clip(CircleShape).border(2.dp, borderColor, CircleShape).padding(8.dp),
        tint = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun GuestProfileView() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Inicia sesión para ver tu perfil", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        // Consider adding a button to navigate to the login screen
    }
}

@Composable
private fun DireccionItem(direccion: DireccionResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = direccion.calle, fontWeight = FontWeight.Bold)
            Text(text = "${direccion.ciudad}, ${direccion.region} ${direccion.codigoPostal}")
        }
    }
}

@Composable
private fun ReviewItem(review: ReviewResponse) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = review.author, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    repeat(5) { index ->
                        Icon(
                            imageVector = if (index < review.calificacion) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = "Estrella de calificación",
                            tint = Color(0xFFFFC400)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = review.comentario)
        }
    }
}