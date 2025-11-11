package com.example.tiendalvlupgamer.ui.screens

import android.util.Base64
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.tiendalvlupgamer.data.network.RetrofitClient
import com.example.tiendalvlupgamer.data.repository.ProfileRepository
import com.example.tiendalvlupgamer.model.DireccionResponse
import com.example.tiendalvlupgamer.model.FullProfileResponse
import com.example.tiendalvlupgamer.model.ReviewResponse
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
            ProfileRepository(RetrofitClient.profileApiService)
        )
    )
) {
    val user by SessionManager.currentUser.collectAsState()
    val fullProfile by profileViewModel.profile.observeAsState()

    // Recargar el perfil cuando se vuelve a esta pantalla
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        InfoRow(label = "Correo Electrónico", value = profile.email)
                        InfoRow(
                            label = "Fecha de Nacimiento",
                            value = profile.birthDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "No especificada"
                        )
                    }
                }
                
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Mis Direcciones", style = MaterialTheme.typography.titleMedium)
                        TextButton(onClick = { navController.navigate(AppScreens.DireccionesScreen.route) }) {
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
                        Text(
                            text = "No tienes direcciones guardadas.", 
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
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
            if (profile.profileImageBase64 != null) {
                val imageBytes = Base64.decode(profile.profileImageBase64, Base64.DEFAULT)
                val bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, yellow, CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto de perfil por defecto",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, yellow, CircleShape)
                        .padding(8.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = onEditProfile, modifier = Modifier.offset(x = (-8).dp, y = 8.dp)) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar perfil")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${profile.name} ${profile.lastName}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "@${profile.username}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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

@Composable
private fun DireccionItem(direccion: DireccionResponse) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = direccion.nombre, fontWeight = FontWeight.Bold)
            Text(text = "${direccion.calle} ${direccion.numeroCasa}")
            if(direccion.numeroDepartamento != null){
                Text(text = "Depto: ${direccion.numeroDepartamento}")
            }
            Text(text = "${direccion.comuna}, ${direccion.ciudad}")
        }
    }
}

@Composable
private fun ReviewItem(review: ReviewResponse) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Calificación: ", fontWeight = FontWeight.Bold)
                (1..5).forEach { star ->
                    Icon(
                        modifier = Modifier.size(16.dp),
                        contentDescription = null,
                        tint = if(star <= review.calificacion) Color(0xFFFFC400) else Color.Gray,
                        imageVector = if (star <= review.calificacion) Icons.Filled.Star else Icons.Outlined.Star
                    )
                }
            }
            Text(text = review.comentario)
            Text(
                text = review.fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}