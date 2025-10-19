// En: app/src/main/java/com/example/levelupgamer/ui/theme/Theme.kt

package com.example.tiendalvlupgamer.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Define la paleta de colores para el tema claro de la app
private val LightColorScheme = lightColorScheme(
    primary = Yellow_LevelUp,    // Color principal para botones, app bars, etc.
    secondary = Grey,
    background = White,          // Color de fondo de la app.
    surface = White,             // Color para superficies como las Cards.
    onPrimary = Black,           // Color del texto sobre el color primario (negro para buen contraste).
    onBackground = Black,        // Color del texto sobre el fondo blanco.
    onSurface = Black,          // Color del texto sobre las superficies blancas.


)

@Composable
fun LevelUpGamerTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography, // Asegúrate de tener tu archivo Type.kt configurado
        content = content
    )
}