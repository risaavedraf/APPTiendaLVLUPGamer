package com.example.tiendalvlupgamer.ui.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendalvlupgamer.data.network.RetrofitClient
import com.example.tiendalvlupgamer.data.repository.ProfileRepository
import com.example.tiendalvlupgamer.viewmodel.ProfileViewModel
import com.example.tiendalvlupgamer.viewmodel.ProfileViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel(
        factory = ProfileViewModelFactory(
            ProfileRepository(RetrofitClient.profileApiService)
        )
    )
) {
    val context = LocalContext.current
    val profile by profileViewModel.profile.observeAsState()
    val updateResult by profileViewModel.updateResult.observeAsState()
    val error by profileViewModel.error.observeAsState()

    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var isInitialLoadComplete by remember { mutableStateOf(false) }

    // Cargar el perfil del usuario la primera vez que se entra a la pantalla
    LaunchedEffect(Unit) {
        profileViewModel.getMyProfile()
    }

    val isFormValid = name.isNotBlank() && lastName.isNotBlank() && birthDate.isNotBlank()

    val calendar = Calendar.getInstance()

    fun showDatePicker() {
        val initialDate = try {
            LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } catch (e: Exception) {
            LocalDate.now()
        }

        DatePickerDialog(
            context,
            { _, year, month, day ->
                val selectedDate = LocalDate.of(year, month + 1, day)
                birthDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            },
            initialDate.year,
            initialDate.monthValue - 1,
            initialDate.dayOfMonth
        ).show()
    }

    // Poblar los campos cuando el perfil se cargue por primera vez
    LaunchedEffect(profile) {
        profile?.let {
            if (!isInitialLoadComplete) {
                name = it.name
                lastName = it.lastName
                birthDate = it.birthDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) ?: ""
                it.birthDate?.let { date ->
                    calendar.set(date.year, date.monthValue - 1, date.dayOfMonth)
                }
                isInitialLoadComplete = true
            }
        }
    }

    // Observar el resultado de la actualización (ÉXITO)
    LaunchedEffect(updateResult) {
        updateResult?.let {
            isLoading = false
            Toast.makeText(context, "Perfil actualizado con éxito", Toast.LENGTH_SHORT).show()
            profileViewModel.onUpdateHandled()
            navController.popBackStack()
        }
    }

    // Observar el resultado de la actualización (ERROR)
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
        if (!isInitialLoadComplete) {
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
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = name.isBlank()
                    )
                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Apellido") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = lastName.isBlank()
                    )
                    OutlinedTextField(
                        value = birthDate,
                        onValueChange = { },
                        label = { Text("Fecha de Nacimiento") },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showDatePicker() },
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker() }) {
                                Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Seleccionar fecha")
                            }
                        },
                        isError = birthDate.isBlank()
                    )
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
