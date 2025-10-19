package com.example.tiendalvlupgamer.view

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiendalvlupgamer.model.RegisterUiState
import com.example.tiendalvlupgamer.viewmodel.RegisterViewModel
import java.util.Calendar
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.navigation.NavController
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material.icons.filled.Person
import com.yalantis.ucrop.UCrop
import java.io.File
import android.app.Activity
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.example.levelupgamer.ui.navigation.AppScreens
import com.example.tiendalvlupgamer.model.local.AppDatabase
import com.example.tiendalvlupgamer.viewmodel.RegisterViewModelFactory


@Composable
fun RegisterScreen(
    navController: NavController,
    onRegister: (RegisterUiState) -> Unit = {}
) {
    val context = LocalContext.current
    val userDao = AppDatabase.get(context).userDao()
    val viewModel: RegisterViewModel = viewModel(factory = RegisterViewModelFactory(userDao))
    val uiState = viewModel.uiState
    var showPassword by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    val yellow = Color(0xFFFFC400)
    val cropLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val uri = UCrop.getOutput(result.data!!)
            uri?.let { viewModel.onProfileImageChange(it) }
        }
    }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val destinationUri = Uri.fromFile(
                File(context.cacheDir, "cropped_profile_${System.currentTimeMillis()}.jpg")
            )

            val options = UCrop.Options().apply {
                setCompressionQuality(90)
                setCircleDimmedLayer(true) // Crop circular como WhatsApp
                setShowCropGrid(false)
                setShowCropFrame(false)
                setHideBottomControls(false)
                setFreeStyleCropEnabled(false)
                setToolbarTitle("Ajustar foto de perfil")
                setToolbarColor(android.graphics.Color.parseColor("#FFC400"))
                setStatusBarColor(android.graphics.Color.parseColor("#FFC400"))
                setActiveControlsWidgetColor(android.graphics.Color.parseColor("#FFC400"))
            }

            val uCrop = UCrop.of(it, destinationUri)
                .withAspectRatio(1f, 1f)
                .withMaxResultSize(800, 800)
                .withOptions(options)

            cropLauncher.launch(uCrop.getIntent(context))
        }
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            imagePickerLauncher.launch("image/*")
        }
    }




    fun showDatePicker() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        DatePickerDialog(context, { _: DatePicker, y: Int, m: Int, d: Int ->
            val formatted = "%02d/%02d/%04d".format(d, m + 1, y)
            viewModel.onBirthDateChange(formatted)
        }, year, month, day).show()
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(modifier = Modifier.fillMaxSize()){
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
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Crear cuenta", style = MaterialTheme.typography.headlineSmall, color = Color.Black)

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .border(2.dp, yellow, CircleShape)
                    .clickable {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
                        } else {
                            permissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                if (uiState.profileImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(uiState.profileImageUri),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .border(2.dp, yellow, CircleShape),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Seleccionar foto",
                        modifier = Modifier.size(50.dp),
                        tint = yellow
                    )
                }
            }

            Text(
                text = "Toca para seleccionar foto",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )

            LabeledTextField(
                value = uiState.name,
                onValueChange = viewModel::onNameChange,
                label = "Nombre",
                modifier = Modifier.fillMaxWidth(0.9f),
                focusColor = yellow,
                error = uiState.nameError
            )

            Spacer(modifier = Modifier.height(8.dp))

            LabeledTextField(
                value = uiState.lastName,
                onValueChange = viewModel::onLastNameChange,
                label = "Apellido",
                modifier = Modifier.fillMaxWidth(0.9f),
                focusColor = yellow,
                error = uiState.lastNameError
            )

            Spacer(modifier = Modifier.height(8.dp))

            LabeledTextField(
                value = uiState.username,
                onValueChange = viewModel::onUsernameChange,
                label = "Username",
                modifier = Modifier.fillMaxWidth(0.9f),
                focusColor = yellow,
                error = uiState.usernameError
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.birthDate,
                onValueChange = { },
                label = { Text("Fecha de nacimiento") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clickable { showDatePicker() },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker() }) {
                        Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Seleccionar fecha")
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = yellow,
                    unfocusedIndicatorColor = Color.Gray,
                    focusedLabelColor = yellow,
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = yellow
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            LabeledTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label = "Correo",
                modifier = Modifier.fillMaxWidth(0.9f),
                focusColor = yellow,
                error = uiState.emailError
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.fillMaxWidth(0.9f)) {
                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = viewModel::onPasswordChange,
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) { }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = yellow,
                        unfocusedIndicatorColor = Color.Gray,
                        focusedLabelColor = yellow,
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = yellow
                    ),
                    isError = uiState.passwordError != null
                )

                // Mostrar requisitos solo cuando el campo tiene foco o contenido
                if (uiState.password.isNotEmpty()) {
                    PasswordRequirements(
                        password = uiState.password,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            val error = uiState.error
            if (error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.tryRegister {
                        // Navegar a la pantalla de login y limpiar el stack
                        navController.navigate(AppScreens.LoginScreen.route) {
                            popUpTo(AppScreens.RegisterScreen.route) { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(48.dp),
                enabled = !uiState.loading,
                colors = ButtonDefaults.buttonColors(containerColor = yellow, contentColor = Color.Black)
            ) {
                if (uiState.loading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black, strokeWidth = 2.dp)
                } else {
                    Text(text = "Registrar", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
fun LabeledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    focusColor: Color = Color(0xFFFFC400),
    error: String? = null
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = focusColor,
                unfocusedIndicatorColor = Color.Gray,
                focusedLabelColor = focusColor,
                unfocusedLabelColor = Color.Gray,
                cursorColor = focusColor
            ),
            isError = error != null
        )

        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}
@Composable
fun PasswordRequirements(
    password: String,
    modifier: Modifier = Modifier
) {
    val requirements = listOf(
        "Mínimo 8 caracteres" to (password.length >= 8),
        "Al menos 1 mayúscula" to password.any { it.isUpperCase() },
        "Al menos 1 minúscula" to password.any { it.isLowerCase() },
        "Al menos 1 número" to password.any { it.isDigit() },
        "Al menos 1 símbolo" to password.any { !it.isLetterOrDigit() }
    )

    Column(modifier = modifier) {
        requirements.forEach { (text, isMet) ->
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isMet) "✓" else "✗",
                    color = if (isMet) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = text,
                    color = if (isMet) Color(0xFF4CAF50) else Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


