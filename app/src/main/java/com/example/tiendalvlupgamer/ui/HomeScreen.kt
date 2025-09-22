package com.example.tiendalvlupgamer.ui

import androidx.compose.foundation.Image
import com.example.tiendalvlupgamer.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel = viewModel()) { // <- Obtenemos el ViewModel aquí

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Mi App Kotlin") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxSize()
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(space = 20.dp)
        ) {
            Text(text = "Bienvenido!")
            Button(onClick = {
                // La View solo llama a la función del ViewModel
                homeViewModel.incrementCounter()
            }) {
                Text(text = "Presioname")
            }

            if (homeViewModel.showMessage) { // <- Usamos el estado del ViewModel
                Text(
                    text = "¡aweonao!",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.Red
                )
                Button(onClick = {
                    // La View solo llama a la función del ViewModel
                    homeViewModel.resetMessageAndLimit()
                }) {
                    Text(text = "¡Volver a empezar!")
                }
            } else {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo app",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height = 150.dp),
                    contentScale = ContentScale.Fit
                )
                Text(text = "Contador: ${homeViewModel.cont}") // <- Usamos el estado del ViewModel
                Text(text = "Límite: ${homeViewModel.limit}") // <- Usamos el estado del ViewModel
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}