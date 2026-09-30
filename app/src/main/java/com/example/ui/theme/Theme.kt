package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldDarkPrimary,
    onPrimary = EmeraldDarkOnPrimary,
    primaryContainer = Color(0xFF064E3B),
    onPrimaryContainer = EmeraldContainer,
    secondary = IndigoMindDark,
    onSecondary = Color(0xFF1E1B4B),
    secondaryContainer = Color(0xFF2E297A),
    onSecondaryContainer = IndigoMindLight,
    tertiary = ModalityAmberDark,
    onTertiary = Color(0xFF451A03),
    tertiaryContainer = Color(0xFF78350F),
    onTertiaryContainer = ModalityAmberLight,
    background = Color(0xFF0B1120),
    surface = Color(0xFF131D31),
    surfaceVariant = Color(0xFF1E293B),
    onBackground = Color(0xFFF1F5F9),
    onSurface = Color(0xFFF8FAFC),
    onSurfaceVariant = Color(0xFFCBD5E1),
    error = EmergencyCrimsonDark,
    onError = Color(0xFF450A0A),
    errorContainer = Color(0xFF7F1D1D),
    onErrorContainer = EmergencyCrimsonLight,
    outline = Color(0xFF334155),
    outlineVariant = Color(0xFF1E293B)
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = EmeraldOnPrimary,
    primaryContainer = EmeraldContainer,
    onPrimaryContainer = EmeraldOnContainer,
    secondary = IndigoMind,
    onSecondary = Color.White,
    secondaryContainer = IndigoMindLight,
    onSecondaryContainer = Color(0xFF1E1B4B),
    tertiary = ModalityAmber,
    onTertiary = Color.White,
    tertiaryContainer = ModalityAmberLight,
    onTertiaryContainer = Color(0xFF451A03),
    background = Color(0xFFF8FAFC),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFF1F5F9),
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    onSurfaceVariant = Color(0xFF475569),
    error = EmergencyCrimson,
    onError = Color.White,
    errorContainer = EmergencyCrimsonLight,
    onErrorContainer = Color(0xFF7F1D1D),
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0)
)

@Composable
fun SimilimumAITheme(
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
