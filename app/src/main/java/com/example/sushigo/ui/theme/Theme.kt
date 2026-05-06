package com.example.sushigo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SalmonLight,
    secondary = DarkGinger,
    tertiary = WasabiGreen,
    background = CharcoalSurface,
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = WarmWhite,
    onSurface = WarmWhite,
    primaryContainer = Color(0xFF3D2018),
    secondaryContainer = Color(0xFF2D2418)
)

private val LightColorScheme = lightColorScheme(
    primary = SalmonPrimary,
    secondary = GingerSecondary,
    tertiary = WasabiGreen,
    background = WarmWhite,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = DarkCharcoal,
    onSurface = DarkCharcoal,
    primaryContainer = Color(0xFFFFEBE6),
    secondaryContainer = Color(0xFFFFF3E0)
)

@Composable
fun SushigoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes, // Подключаем наши формы со скруглениями
        content = content
    )
}
