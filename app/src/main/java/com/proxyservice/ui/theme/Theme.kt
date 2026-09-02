package com.proxyservice.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF64B5F6),
    primaryContainer = Color(0xFF1E3A5F),
    secondary = Color(0xFF81C784),
    secondaryContainer = Color(0xFF1B3A1C),
    tertiary = Color(0xFFFFB74D),
    tertiaryContainer = Color(0xFF3E2E00),
    surface = Color(0xFF121212),
    surfaceVariant = Color(0xFF1E1E1E),
    background = Color(0xFF121212),
    error = Color(0xFFEF5350),
    onPrimary = Color(0xFF000000),
    onPrimaryContainer = Color(0xFFFFFFFF),
    onSecondary = Color(0xFF000000),
    onSecondaryContainer = Color(0xFFFFFFFF),
    onTertiary = Color(0xFF000000),
    onTertiaryContainer = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),
    onSurfaceVariant = Color(0xFFB0B0B0),
    onBackground = Color(0xFFFFFFFF),
    onError = Color(0xFF000000),
    onErrorContainer = Color(0xFFFFFFFF),
    outline = Color(0xFF666666),
    outlineVariant = Color(0xFF444444),
    scrim = Color(0xFF000000),
    shadow = Color(0xFF000000),
    inverseSurface = Color(0xFFE0E0E0),
    inverseOnSurface = Color(0xFF121212),
    inversePrimary = Color(0xFF1976D2)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1976D2),
    primaryContainer = Color(0xFFBBDEFB),
    secondary = Color(0xFF388E3C),
    secondaryContainer = Color(0xFFC8E6C9),
    tertiary = Color(0xFFF57C00),
    tertiaryContainer = Color(0xFFFFE0B2),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFF5F5F5),
    background = Color(0xFFFFFFFF),
    error = Color(0xFFD32F2F),
    onPrimary = Color(0xFFFFFFFF),
    onPrimaryContainer = Color(0xFF000000),
    onSecondary = Color(0xFFFFFFFF),
    onSecondaryContainer = Color(0xFF000000),
    onTertiary = Color(0xFFFFFFFF),
    onTertiaryContainer = Color(0xFF000000),
    onSurface = Color(0xFF121212),
    onSurfaceVariant = Color(0xFF444444),
    onBackground = Color(0xFF121212),
    onError = Color(0xFFFFFFFF),
    onErrorContainer = Color(0xFFFFFFFF),
    outline = Color(0xFF888888),
    outlineVariant = Color(0xFFCCCCCC),
    scrim = Color(0xFF000000),
    shadow = Color(0xFF000000),
    inverseSurface = Color(0xFF121212),
    inverseOnSurface = Color(0xFFFFFFFF),
    inversePrimary = Color(0xFF64B5F6)
)

@Composable
fun ProxyTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = androidx.compose.material3.Typography,
        content = content
    )
}