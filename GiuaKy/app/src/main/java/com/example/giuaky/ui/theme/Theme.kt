package com.example.giuaky.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(
    primary = AccentOrange,
    onPrimary = AppSurface,
    secondary = AppSurfaceSoft,
    tertiary = AccentOrangeSoft,
    background = AppBackground,
    surface = AppSurface,
    onSurface = TextPrimary,
    onBackground = TextPrimary,
    error = DangerRed
)

@Composable
fun GiuaKyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}
