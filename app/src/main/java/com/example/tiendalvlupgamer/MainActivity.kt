package com.example.tiendalvlupgamer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tiendalvlupgamer.view.LoginScreen
import com.example.tiendalvlupgamer.view.RegisterScreen
import com.example.tiendalvlupgamer.view.WelcomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppContent()
        }
    }
}

@Composable
fun AppContent() {
    val navController = rememberNavController()

    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(navController = navController, startDestination = "welcome") {
            composable("welcome") {
                WelcomeScreen(
                    onNavigateRegister = { navController.navigate("register") },
                    onNavigateLogin = { navController.navigate("login") },
                    onNavigateGuest = { /* navController.navigate("guest") */ }
                )
            }
            composable("register") {
                RegisterScreen(navController = navController)
            }
            composable("login") {
                LoginScreen(navController = navController)
            }
        }
    }
}
