package com.example.praktam2_2417051039.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    background = Background,
    surface = Surface,
    error = Danger,
    onPrimary = Surface,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun PrakTAM2_2417051039Theme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography,
        content = content
    )
}