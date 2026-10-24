package com.example.designsystem.theme

import androidx.compose.ui.graphics.Color

// ================================
// Default Brand Colors
// ================================

internal val Primary = Color(0xFF000000)
internal val OnPrimary = Color(0xFFFFFFFF)
internal val PrimaryContainer = Color(0xFFE5E5E5)
internal val OnPrimaryContainer = Color(0xFF000000)

internal val Secondary = Color(0xFF333333)
internal val OnSecondary = Color(0xFFFFFFFF)
internal val SecondaryContainer = Color(0xFFF0F0F0)
internal val OnSecondaryContainer = Color(0xFF1A1A1A)

internal val Tertiary = Color(0xFF555555)
internal val OnTertiary = Color(0xFFFFFFFF)
internal val TertiaryContainer = Color(0xFFEAEAEA)
internal val OnTertiaryContainer = Color(0xFF1A1A1A)

// ================================
// Status Colors
// ================================

internal val Success = Color(0xFF2E7D32)

internal val Error = Color(0xFFB3261E)
internal val OnError = Color(0xFFFFFFFF)
internal val ErrorContainer = Color(0xFFF9DEDC)
internal val OnErrorContainer = Color(0xFF410E0B)

// ================================
// Default Light Theme
// ================================

internal val LightBackground = Color(0xFFFFFFFF)
internal val LightOnBackground = Color(0xFF000000)

internal val LightSurface = Color(0xFFFFFFFF)
internal val LightOnSurface = Color(0xFF000000)

internal val LightSurfaceVariant = Color(0xFFF5F5F5)
internal val LightOnSurfaceVariant = Color(0xFF555555)

internal val LightOutline = Color(0xFFD6D6D6)

// ================================
// Default Dark Theme
// ================================

internal val DarkBackground = Color(0xFF000000)
internal val DarkOnBackground = Color(0xFFFFFFFF)

internal val DarkSurface = Color(0xFF121212)
internal val DarkOnSurface = Color(0xFFFFFFFF)

internal val DarkSurfaceVariant = Color(0xFF242424)
internal val DarkOnSurfaceVariant = Color(0xFFCCCCCC)

internal val DarkOutline = Color(0xFF555555)

// ================================
// Additional Colors
// ================================

internal val BackgroundRedLight = Color(0xFFF5F5F5)
internal val BackgroundGrayLight = Color(0xFFF5F5F5)
internal val BackgroundGrayDark = Color(0xFF202020)

internal val White = Color(0xFFFFFFFF)
internal val Black = Color(0xFF000000)

// ================================
// Theme Color Model
// ================================

internal data class AppThemeColors(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,

    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,

    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,

    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val outline: Color
)

// ================================
// Pink Theme
// ================================

internal val PinkThemeColors = AppThemeColors(
    primary = Color(0xFFC2185B),
    onPrimary = White,
    primaryContainer = Color(0xFFF8BBD0),
    onPrimaryContainer = Color(0xFF3E001D),

    secondary = Color(0xFFAD1457),
    onSecondary = White,
    secondaryContainer = Color(0xFFFCE4EC),
    onSecondaryContainer = Color(0xFF3E001D),

    tertiary = Color(0xFF880E4F),
    onTertiary = White,
    tertiaryContainer = Color(0xFFF3D9E3),
    onTertiaryContainer = Color(0xFF3E001D),

    background = Color(0xFFFFF1F5),
    onBackground = Color(0xFF2D101C),
    surface = Color(0xFFFFF8FA),
    onSurface = Color(0xFF2D101C),
    surfaceVariant = Color(0xFFF8E5EC),
    onSurfaceVariant = Color(0xFF70515E),
    outline = Color(0xFFD8B8C5)
)

// ================================
// Green Theme
// ================================

internal val GreenThemeColors = AppThemeColors(
    primary = Color(0xFF2E7D32),
    onPrimary = White,
    primaryContainer = Color(0xFFC8E6C9),
    onPrimaryContainer = Color(0xFF08210A),

    secondary = Color(0xFF388E3C),
    onSecondary = White,
    secondaryContainer = Color(0xFFE8F5E9),
    onSecondaryContainer = Color(0xFF102B12),

    tertiary = Color(0xFF1B5E20),
    onTertiary = White,
    tertiaryContainer = Color(0xFFDCEFE0),
    onTertiaryContainer = Color(0xFF102B12),

    background = Color(0xFFF1F8F1),
    onBackground = Color(0xFF102B12),
    surface = Color(0xFFF9FCF9),
    onSurface = Color(0xFF102B12),
    surfaceVariant = Color(0xFFE4EFE4),
    onSurfaceVariant = Color(0xFF506652),
    outline = Color(0xFFB8CDB9)
)

// ================================
// Blue Theme
// ================================

internal val BlueThemeColors = AppThemeColors(
    primary = Color(0xFF1565C0),
    onPrimary = White,
    primaryContainer = Color(0xFFBBDEFB),
    onPrimaryContainer = Color(0xFF001D35),

    secondary = Color(0xFF1976D2),
    onSecondary = White,
    secondaryContainer = Color(0xFFE3F2FD),
    onSecondaryContainer = Color(0xFF001D35),

    tertiary = Color(0xFF0D47A1),
    onTertiary = White,
    tertiaryContainer = Color(0xFFD9E8FA),
    onTertiaryContainer = Color(0xFF001D35),

    background = Color(0xFFF0F7FF),
    onBackground = Color(0xFF001D35),
    surface = Color(0xFFF8FBFF),
    onSurface = Color(0xFF001D35),
    surfaceVariant = Color(0xFFE3EFFB),
    onSurfaceVariant = Color(0xFF4D6278),
    outline = Color(0xFFB7CBE0)
)

// ================================
// Purple Theme
// ================================

internal val PurpleThemeColors = AppThemeColors(
    primary = Color(0xFF6A1B9A),
    onPrimary = White,
    primaryContainer = Color(0xFFE1BEE7),
    onPrimaryContainer = Color(0xFF24003A),

    secondary = Color(0xFF7B1FA2),
    onSecondary = White,
    secondaryContainer = Color(0xFFF3E5F5),
    onSecondaryContainer = Color(0xFF24003A),

    tertiary = Color(0xFF4A148C),
    onTertiary = White,
    tertiaryContainer = Color(0xFFE8DDF0),
    onTertiaryContainer = Color(0xFF24003A),

    background = Color(0xFFF8F2FC),
    onBackground = Color(0xFF24003A),
    surface = Color(0xFFFCF9FF),
    onSurface = Color(0xFF24003A),
    surfaceVariant = Color(0xFFEFE4F5),
    onSurfaceVariant = Color(0xFF66516F),
    outline = Color(0xFFD0BCD9)
)