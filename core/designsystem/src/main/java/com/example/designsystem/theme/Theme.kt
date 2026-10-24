package com.example.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class AppTheme {
    Default,
    Pink,
    Green,
    Blue,
    Purple
}

private fun AppTheme.colors(): AppThemeColors? {
    return when (this) {
        AppTheme.Default -> null
        AppTheme.Pink -> PinkThemeColors
        AppTheme.Green -> GreenThemeColors
        AppTheme.Blue -> BlueThemeColors
        AppTheme.Purple -> PurpleThemeColors
    }
}

@Composable
fun BanumusaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    appTheme: AppTheme = AppTheme.Default,
    content: @Composable () -> Unit
) {
    val selectedColors = appTheme.colors()

    android.util.Log.d(
        "ThemeDebug",
        "appTheme=$appTheme, primary=${selectedColors?.primary}"
    )
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = selectedColors?.primary ?: Primary,
            onPrimary = selectedColors?.onPrimary ?: OnPrimary,
            primaryContainer = selectedColors?.primaryContainer ?: PrimaryContainer,
            onPrimaryContainer = selectedColors?.onPrimaryContainer ?: OnPrimaryContainer,

            secondary = selectedColors?.secondary ?: Secondary,
            onSecondary = selectedColors?.onSecondary ?: OnSecondary,
            secondaryContainer = selectedColors?.secondaryContainer ?: SecondaryContainer,
            onSecondaryContainer = selectedColors?.onSecondaryContainer
                ?: OnSecondaryContainer,

            tertiary = selectedColors?.tertiary ?: Tertiary,
            onTertiary = selectedColors?.onTertiary ?: OnTertiary,
            tertiaryContainer = selectedColors?.tertiaryContainer ?: TertiaryContainer,
            onTertiaryContainer = selectedColors?.onTertiaryContainer
                ?: OnTertiaryContainer,

            error = Error,
            onError = OnError,
            errorContainer = ErrorContainer,
            onErrorContainer = OnErrorContainer,

            background = if (selectedColors == null) {
                DarkBackground
            } else {
                Color(0xFF171217)
            },
            onBackground = if (selectedColors == null) {
                DarkOnBackground
            } else {
                Color(0xFFFFF7FA)
            },

            surface = if (selectedColors == null) {
                DarkSurface
            } else {
                Color(0xFF211B21)
            },
            onSurface = if (selectedColors == null) {
                DarkOnSurface
            } else {
                Color(0xFFFFF7FA)
            },

            surfaceVariant = if (selectedColors == null) {
                DarkSurfaceVariant
            } else {
                Color(0xFF352B34)
            },
            onSurfaceVariant = if (selectedColors == null) {
                DarkOnSurfaceVariant
            } else {
                Color(0xFFD8C2D2)
            },

            outline = if (selectedColors == null) {
                DarkOutline
            } else {
                Color(0xFF8D7487)
            }
        )
    } else {
        lightColorScheme(
            primary = selectedColors?.primary ?: Primary,
            onPrimary = selectedColors?.onPrimary ?: OnPrimary,
            primaryContainer = selectedColors?.primaryContainer ?: PrimaryContainer,
            onPrimaryContainer = selectedColors?.onPrimaryContainer ?: OnPrimaryContainer,

            secondary = selectedColors?.secondary ?: Secondary,
            onSecondary = selectedColors?.onSecondary ?: OnSecondary,
            secondaryContainer = selectedColors?.secondaryContainer ?: SecondaryContainer,
            onSecondaryContainer = selectedColors?.onSecondaryContainer
                ?: OnSecondaryContainer,

            tertiary = selectedColors?.tertiary ?: Tertiary,
            onTertiary = selectedColors?.onTertiary ?: OnTertiary,
            tertiaryContainer = selectedColors?.tertiaryContainer ?: TertiaryContainer,
            onTertiaryContainer = selectedColors?.onTertiaryContainer
                ?: OnTertiaryContainer,

            error = Error,
            onError = OnError,
            errorContainer = ErrorContainer,
            onErrorContainer = OnErrorContainer,

            background = selectedColors?.background ?: LightBackground,
            onBackground = selectedColors?.onBackground ?: LightOnBackground,

            surface = selectedColors?.surface ?: LightSurface,
            onSurface = selectedColors?.onSurface ?: LightOnSurface,

            surfaceVariant = selectedColors?.surfaceVariant ?: LightSurfaceVariant,
            onSurfaceVariant = selectedColors?.onSurfaceVariant
                ?: LightOnSurfaceVariant,

            outline = selectedColors?.outline ?: LightOutline
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FfsTypography,
        content = content
    )
}