package com.example.tiendalvlupgamer.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiendalvlupgamer.R
import com.example.tiendalvlupgamer.model.WelcomeNavigation
import com.example.tiendalvlupgamer.ui.WelcomeViewModel

@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel = viewModel(),
    onNavigateRegister: () -> Unit = {},
    onNavigateLogin: () -> Unit = {},
    onNavigateGuest: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(viewModel) {
        viewModel.navigation.collect { nav ->
            when (nav) {
                is WelcomeNavigation.ToRegister -> onNavigateRegister()
                is WelcomeNavigation.ToLogin -> onNavigateLogin()
                is WelcomeNavigation.ToGuest -> onNavigateGuest()
            }
        }
    }

    val yellow = Color(0xFFFFC400)

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // centra verticalmente
        ) {
            Text(
                text = "¡Bienvenido a LVL UP GAMER!",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo tienda",
                modifier = Modifier.size(140.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(28.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {onNavigateRegister() },
                    modifier = Modifier.fillMaxWidth(0.9f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = yellow, contentColor = Color.Black)
                ) {
                    Text(text = "Registrarte", style = MaterialTheme.typography.bodyLarge)
                }

                Button(
                    onClick = { onNavigateLogin() },
                    modifier = Modifier.fillMaxWidth(0.9f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = yellow, contentColor = Color.Black)
                ) {
                    Text(text = "Iniciar sesión", style = MaterialTheme.typography.bodyLarge)
                }

                Button(
                    onClick = { viewModel.onGuest() },
                    modifier = Modifier.fillMaxWidth(0.9f).height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = yellow, contentColor = Color.Black)
                ) {
                    Text(text = "Entrar como invitado", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen()
}