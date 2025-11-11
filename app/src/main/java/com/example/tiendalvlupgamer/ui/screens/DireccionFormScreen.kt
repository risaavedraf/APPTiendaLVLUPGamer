package com.example.tiendalvlupgamer.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.tiendalvlupgamer.data.repository.DireccionRepository
import com.example.tiendalvlupgamer.model.DireccionRequest
import com.example.tiendalvlupgamer.viewmodel.DireccionViewModel
import com.example.tiendalvlupgamer.viewmodel.DireccionViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DireccionFormScreen(
    navController: NavController,
    direccionId: Long,
    viewModel: DireccionViewModel = viewModel(
        factory = DireccionViewModelFactory(
            DireccionRepository(RetrofitClient.direccionApiService)
        )
    )
) {
    val context = LocalContext.current
    val isEditing = direccionId != 0L

    val direccionToEdit by viewModel.direccion.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()
    val operationSuccess by viewModel.operationSuccess.observeAsState(false)

    var nombre by remember { mutableStateOf("") }
    var nombreDestinatario by remember { mutableStateOf("") }
    var calle by remember { mutableStateOf("") }
    var numeroCasa by remember { mutableStateOf("") }
    var numeroDepartamento by remember { mutableStateOf("") }
    var comuna by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var codigoPostal by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (isEditing) {
            viewModel.loadDireccionById(direccionId)
        }
    }

    LaunchedEffect(direccionToEdit) {
        direccionToEdit?.let {
            nombre = it.nombre
            nombreDestinatario = it.nombreDestinatario
            calle = it.calle
            numeroCasa = it.numeroCasa
            numeroDepartamento = it.numeroDepartamento ?: ""
            comuna = it.comuna
            ciudad = it.ciudad
            region = it.region
            codigoPostal = it.codigoPostal
        }
    }

    LaunchedEffect(operationSuccess) {
        if (operationSuccess) {
            val message = if (isEditing) "Dirección guardada" else "Dirección creada"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.onOperationConsumed()
            navController.popBackStack()
        }
    }

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.onOperationConsumed()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Editar Dirección" else "Nueva Dirección") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre (ej: Casa, Trabajo)") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = nombreDestinatario, onValueChange = { nombreDestinatario = it }, label = { Text("Nombre del destinatario") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = calle, onValueChange = { calle = it }, label = { Text("Calle") }, modifier = Modifier.fillMaxWidth())
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)){
                    OutlinedTextField(value = numeroCasa, onValueChange = { numeroCasa = it }, label = { Text("Número") }, modifier = Modifier.weight(1f))
                    OutlinedTextField(value = numeroDepartamento, onValueChange = { numeroDepartamento = it }, label = { Text("Depto. (Opcional)") }, modifier = Modifier.weight(1f))
                }
                OutlinedTextField(value = comuna, onValueChange = { comuna = it }, label = { Text("Comuna") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = ciudad, onValueChange = { ciudad = it }, label = { Text("Ciudad") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = region, onValueChange = { region = it }, label = { Text("Región") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = codigoPostal, onValueChange = { codigoPostal = it }, label = { Text("Código Postal") }, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val request = DireccionRequest(
                            nombre = nombre,
                            nombreDestinatario = nombreDestinatario,
                            calle = calle,
                            numeroCasa = numeroCasa,
                            numeroDepartamento = numeroDepartamento.takeIf { it.isNotBlank() },
                            comuna = comuna,
                            ciudad = ciudad,
                            region = region,
                            codigoPostal = codigoPostal
                        )
                        if (isEditing) {
                            viewModel.updateDireccion(direccionId, request)
                        } else {
                            viewModel.createDireccion(request)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Text(if (isLoading) "Guardando..." else "Guardar Dirección")
                }
            }
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
