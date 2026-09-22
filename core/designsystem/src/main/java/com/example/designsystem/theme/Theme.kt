package com.example.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,

    background = LightBackground,
    onBackground = OnBackground,

    surface = LightSurface,
    onSurface = OnSurface,

    outline = Outline,

    error = Error,
    onError = OnError,
)

@Composable
fun BanumusaTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = BanumusaTypography,
        content = content,
    )
}