package com.example.primetestertest.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.primetestertest.FontScale
import com.example.primetestertest.ThemeMode

private val LightColorScheme = lightColorScheme(
    primary = DeepBlue,
    background = Color(0xFFF5F7FA),
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = Color(0xFF102027),
    onSurface = Color(0xFF102027)
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimeBlue,
    background = NightBlue,
    surface = DeepBlue,
    onPrimary = Color.White,
    onBackground = TextPrimary,
    onSurface = Color.White
)

@Composable
fun PrimeTesterTestTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    fontScale: FontScale = FontScale.NORMAL,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val scaleFactor = if (fontScale == FontScale.LARGE) 1.3f else 1.0f

    // Wir nehmen AppTypography als Basis und skalieren JEDEN Style
    val scaledTypography = Typography(
        displayLarge = AppTypography.displayLarge.copy(fontSize = AppTypography.displayLarge.fontSize * scaleFactor),
        displayMedium = AppTypography.displayMedium.copy(fontSize = AppTypography.displayMedium.fontSize * scaleFactor),
        displaySmall = AppTypography.displaySmall.copy(fontSize = AppTypography.displaySmall.fontSize * scaleFactor),
        headlineLarge = AppTypography.headlineLarge.copy(fontSize = AppTypography.headlineLarge.fontSize * scaleFactor),
        headlineMedium = AppTypography.headlineMedium.copy(fontSize = AppTypography.headlineMedium.fontSize * scaleFactor),
        headlineSmall = AppTypography.headlineSmall.copy(fontSize = AppTypography.headlineSmall.fontSize * scaleFactor),
        titleLarge = AppTypography.titleLarge.copy(fontSize = AppTypography.titleLarge.fontSize * scaleFactor),
        titleMedium = AppTypography.titleMedium.copy(fontSize = AppTypography.titleMedium.fontSize * scaleFactor),
        titleSmall = AppTypography.titleSmall.copy(fontSize = AppTypography.titleSmall.fontSize * scaleFactor),
        bodyLarge = AppTypography.bodyLarge.copy(fontSize = AppTypography.bodyLarge.fontSize * scaleFactor),
        bodyMedium = AppTypography.bodyMedium.copy(fontSize = AppTypography.bodyMedium.fontSize * scaleFactor),
        bodySmall = AppTypography.bodySmall.copy(fontSize = AppTypography.bodySmall.fontSize * scaleFactor),
        labelLarge = AppTypography.labelLarge.copy(fontSize = AppTypography.labelLarge.fontSize * scaleFactor),
        labelMedium = AppTypography.labelMedium.copy(fontSize = AppTypography.labelMedium.fontSize * scaleFactor),
        labelSmall = AppTypography.labelSmall.copy(fontSize = AppTypography.labelSmall.fontSize * scaleFactor)
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = scaledTypography,
        content = content
    )
}
