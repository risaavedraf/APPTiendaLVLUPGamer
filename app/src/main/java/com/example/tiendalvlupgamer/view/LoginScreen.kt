package com.example.tiendalvlupgamer.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tiendalvlupgamer.ui.navigation.AppScreens
import com.example.tiendalvlupgamer.model.LoginUiState
import com.example.tiendalvlupgamer.model.local.AppDatabase
import com.example.tiendalvlupgamer.viewmodel.LoginViewModel
import com.example.tiendalvlupgamer.viewmodel.LoginViewModelFactory
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@Composable
fun LoginScreen(
    navController: NavController,
    onLogin: (LoginUiState) -> Unit = {}
) {
    val userDao = AppDatabase.get(LocalContext.current).userDao()
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(userDao))
    val uiState = viewModel.uiState
    var showPassword by remember { mutableStateOf(false) }
    val yellow = Color(0xFFFFC400)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Botón de retroceso
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = yellow
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "TIENDA LVL UP GAMER",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Campo de Email o Username
            Column(modifier = Modifier.fillMaxWidth(0.9f)) {
                OutlinedTextField(
                    value = uiState.emailOrUsername,
                    onValueChange = viewModel::onEmailOrUsernameChange,
                    label = { Text("Correo o Username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("emailOrUsernameInput"),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = yellow,
                        unfocusedIndicatorColor = Color.Gray,
                        focusedLabelColor = yellow,
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = yellow,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    isError = uiState.emailOrUsernameError != null
                )

                if (uiState.emailOrUsernameError != null) {
                    Text(
                        text = uiState.emailOrUsernameError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Contraseña
            Column(modifier = Modifier.fillMaxWidth(0.9f)) {
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = viewModel::onPasswordChange,
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                        }
                    },
                    modifier = Modifier.fillMaxWidth().testTag("passwordInput"),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = yellow,
                        unfocusedIndicatorColor = Color.Gray,
                        focusedLabelColor = yellow,
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = yellow,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    isError = uiState.passwordError != null
                )

                if (uiState.passwordError != null) {
                    Text(
                        text = uiState.passwordError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Mensaje de error general
            if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Botón de Iniciar Sesión
            Button(
                onClick = {
                    viewModel.tryLogin {
                        // ¡CORREGIDO! Se llama al callback onLogin
                        onLogin(uiState)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(48.dp)
                    .testTag("loginButton"),
                enabled = !uiState.loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = yellow,
                    contentColor = Color.Black
                )
            ) {
                if (uiState.loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Iniciar Sesión",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { navController.navigate(AppScreens.RegisterScreen.route) }
            ) {
                Text(
                    text = "¿No tienes cuenta? Regístrate",
                    color = yellow
                )
            }
        }
    }
}
