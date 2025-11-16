package com.example.tiendalvlupgamer.ui.screens

import android.app.DatePickerDialog
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendalvlupgamer.data.network.RetrofitClient
import com.example.tiendalvlupgamer.data.repository.ImagenRepository
import com.example.tiendalvlupgamer.data.repository.ProfileRepository
import com.example.tiendalvlupgamer.viewmodel.ProfileViewModel
import com.example.tiendalvlupgamer.viewmodel.ProfileViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(
            ProfileRepository(RetrofitClient.profileApiService),
            ImagenRepository(RetrofitClient.imagenApiService)
        )
    )
) {
    val context = LocalContext.current
    val profile by profileViewModel.profile.observeAsState()
    val updateResult by profileViewModel.updateResult.observeAsState()
    val error by profileViewModel.error.observeAsState()
    val imageUploadResult by profileViewModel.imageUploadResult.observeAsState()

    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isInitialLoadComplete by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                isLoading = true // Mostrar indicador de carga
                val inputStream = context.contentResolver.openInputStream(it)
                val bytes = inputStream?.readBytes()
                inputStream?.close()

                if (bytes != null && profile != null) {
                    val requestBody = bytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
                    val part = MultipartBody.Part.createFormData("file", "profile_image.jpg", requestBody)
                    profileViewModel.uploadProfileImage(profile!!.id, part)
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        profileViewModel.getMyProfile()
    }

    val isFormValid = name.isNotBlank() && lastName.isNotBlank() && birthDate.isNotBlank()

    val calendar = Calendar.getInstance()

    fun showDatePicker() { /* ... (código sin cambios) */ }

    LaunchedEffect(profile) {
        profile?.let {
            if (!isInitialLoadComplete) {
                name = it.name
                lastName = it.lastName
                birthDate = it.birthDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) ?: ""
            }
            isInitialLoadComplete = true
        }
    }

    // **CORRECCIÓN:** Manejar el fin de la carga para la subida de imagen
    LaunchedEffect(imageUploadResult) {
        imageUploadResult?.let {
            isLoading = false // Detener la carga aquí
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            profileViewModel.onImageUploadHandled()
        }
    }

    // **CORRECCIÓN:** Restaurar el manejo de carga para la actualización de datos
    LaunchedEffect(updateResult) {
        updateResult?.let {
            isLoading = false
            Toast.makeText(context, "Perfil actualizado con éxito", Toast.LENGTH_SHORT).show()
            profileViewModel.onUpdateHandled()
            navController.popBackStack()
        }
    }

    LaunchedEffect(error) {
        error?.let {
            isLoading = false
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            profileViewModel.onUpdateHandled()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (!isInitialLoadComplete && profile == null) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        val imageString = profile?.profileImageBase64
                        if (imageString != null && imageString.contains(",")) {
                            val base64Image = imageString.substringAfter(delimiter = ',')
                            val imageBytes = Base64.decode(base64Image, Base64.DEFAULT)
                            val bitmap = android.graphics.BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Foto de perfil",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                    .clickable { imagePickerLauncher.launch("image/*") },
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Foto de perfil por defecto",
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.Gray, CircleShape)
                                    .clickable { imagePickerLauncher.launch("image/*") }
                                    .padding(8.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar imagen",
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .background(MaterialTheme.colorScheme.surface, CircleShape)
                                .padding(4.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = birthDate, onValueChange = {}, label = { Text("Fecha de Nacimiento") }, readOnly = true, modifier = Modifier.fillMaxWidth().clickable { showDatePicker() }, trailingIcon = { Icon(Icons.Default.DateRange, "Seleccionar fecha") })
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            isLoading = true
                            profileViewModel.updateMyProfile(name, lastName, birthDate)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading && isFormValid
                    ) {
                        Text("Guardar Cambios")
                    }
                }
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}