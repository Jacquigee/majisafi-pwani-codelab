package com.pwani.majisafi.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = OceanPrimary,
    secondary = OceanSecondary,
    tertiary = OceanTertiary,
    background = SandBackground,
    surface = SurfaceLight,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF191C1C),
    onSurface = Color(0xFF191C1C)
)

private val DarkColorScheme = darkColorScheme(
    primary = OceanSecondary,
    secondary = OceanTertiary,
    background = Color(0xFF121414),
    surface = Color(0xFF1A1C1C),
    onPrimary = Color.White,
    onBackground = Color(0xFFE1E3E3),
    onSurface = Color(0xFFE1E3E3)
)

@Composable
fun MajiSafiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
