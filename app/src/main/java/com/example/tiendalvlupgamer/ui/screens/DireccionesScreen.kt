package com.example.tiendalvlupgamer.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendalvlupgamer.data.network.RetrofitClient
import com.example.tiendalvlupgamer.data.repository.DireccionRepository
import com.example.tiendalvlupgamer.model.DireccionResponse
import com.example.tiendalvlupgamer.ui.navigation.AppScreens
import com.example.tiendalvlupgamer.viewmodel.DireccionViewModel
import com.example.tiendalvlupgamer.viewmodel.DireccionViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DireccionesScreen(
    navController: NavController,
    viewModel: DireccionViewModel = viewModel(
        factory = DireccionViewModelFactory(
            DireccionRepository(RetrofitClient.direccionApiService)
        )
    )
) {
    val context = LocalContext.current
    val direcciones by viewModel.direcciones.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()
    val operationSuccess by viewModel.operationSuccess.observeAsState(false)

    LaunchedEffect(Unit) {
        viewModel.loadDirecciones()
    }

    LaunchedEffect(operationSuccess) {
        if (operationSuccess) {
            Toast.makeText(context, "Operación exitosa", Toast.LENGTH_SHORT).show()
            viewModel.loadDirecciones() // Recargar la lista
            viewModel.onOperationConsumed()
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
                title = { Text("Gestionar Direcciones") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(AppScreens.DireccionFormScreen.createRoute(0L)) }) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Dirección")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading && direcciones.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (direcciones.isEmpty()) {
                Text("No tienes direcciones guardadas.", modifier = Modifier.align(Alignment.Center))
            }
            else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(direcciones, key = { it.id }) { direccion ->
                        DireccionManagementItem(navController, direccion, viewModel)
                    }
                }
            }
        }
    }
}

@Composable
private fun DireccionManagementItem(
    navController: NavController,
    direccion: DireccionResponse,
    viewModel: DireccionViewModel
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = direccion.nombre, fontWeight = FontWeight.Bold)
                Text(text = "${direccion.calle} ${direccion.numeroCasa}")
                if (!direccion.numeroDepartamento.isNullOrEmpty()) {
                    Text(text = "Depto: ${direccion.numeroDepartamento}")
                }
                Text(text = "${direccion.comuna}, ${direccion.ciudad}")
            }
            Row {
                IconButton(onClick = { navController.navigate(AppScreens.DireccionFormScreen.createRoute(direccion.id)) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = { viewModel.deleteDireccion(direccion.id) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
