package com.example.bluromatic.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary      = Color(0xFF2ABFBF),
    secondary    = Color(0xFF03DAC6),
    background   = Color(0xFFF0F0F0),
    surface      = Color.White,
    onPrimary    = Color.White,
    onBackground = Color(0xFF1A1A1A),
    onSurface    = Color(0xFF1A1A1A)
)

@Composable
fun BlurOMaticTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content     = content
    )
}