package com.gitdrip.app.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private fun c(v: Long) = Color(v)

/** Fixed brand palette (dynamic color dropped on purpose; see plan Track D). Every M3 slot is mapped so unrestyled screens stay coherent. */
private val DarkScheme: ColorScheme = darkColorScheme(
    primary = c(Palette.DarkPrimary), onPrimary = c(Palette.DarkOnPrimary),
    primaryContainer = c(0xFF3730A3), onPrimaryContainer = c(0xFFE0E7FF),
    secondary = c(Palette.DarkSecondary), onSecondary = c(0xFF04222A),
    secondaryContainer = c(0xFF0E4A5A), onSecondaryContainer = c(0xFFCFFAFE),
    tertiary = c(Palette.DarkTertiary), onTertiary = c(0xFF3B0A24),
    tertiaryContainer = c(0xFF7A1D4D), onTertiaryContainer = c(0xFFFCE7F3),
    background = c(Palette.DarkBgTop), onBackground = c(Palette.DarkOnBg),
    surface = c(Palette.DarkSurface), onSurface = c(Palette.DarkOnBg),
    surfaceVariant = c(Palette.DarkSurfaceVariant), onSurfaceVariant = c(Palette.DarkOnVariant),
    surfaceTint = c(Palette.DarkPrimary),
    inverseSurface = c(Palette.DarkOnBg), inverseOnSurface = c(Palette.DarkBgTop), inversePrimary = c(0xFF4F46E5),
    error = c(Palette.DarkError), onError = c(0xFF3F0A0A),
    errorContainer = c(0xFF7F1D1D), onErrorContainer = c(0xFFFEE2E2),
    outline = c(Palette.DarkOutline), outlineVariant = c(Palette.DarkOutlineVariant),
    scrim = Color.Black,
    surfaceContainerLowest = c(Palette.DarkContainerLowest), surfaceContainerLow = c(Palette.DarkContainerLow),
    surfaceContainer = c(Palette.DarkContainer), surfaceContainerHigh = c(Palette.DarkContainerHigh),
    surfaceContainerHighest = c(Palette.DarkContainerHighest),
)

private val LightScheme: ColorScheme = lightColorScheme(
    primary = c(Palette.LightPrimary), onPrimary = c(Palette.LightOnPrimary),
    primaryContainer = c(0xFFE0E7FF), onPrimaryContainer = c(0xFF1E1B6B),
    secondary = c(Palette.LightSecondary), onSecondary = Color.White,
    secondaryContainer = c(0xFFCFFAFE), onSecondaryContainer = c(0xFF083344),
    tertiary = c(Palette.LightTertiary), onTertiary = Color.White,
    tertiaryContainer = c(0xFFFCE7F3), onTertiaryContainer = c(0xFF500724),
    background = c(Palette.LightBgTop), onBackground = c(Palette.LightOnBg),
    surface = c(Palette.LightSurface), onSurface = c(Palette.LightOnBg),
    surfaceVariant = c(Palette.LightSurfaceVariant), onSurfaceVariant = c(Palette.LightOnVariant),
    surfaceTint = c(Palette.LightPrimary),
    inverseSurface = c(0xFF1E293B), inverseOnSurface = c(0xFFF1F5F9), inversePrimary = c(0xFFA5B4FC),
    error = c(Palette.LightError), onError = Color.White,
    errorContainer = c(0xFFFEE2E2), onErrorContainer = c(0xFF7F1D1D),
    outline = c(Palette.LightOutline), outlineVariant = c(Palette.LightOutlineVariant),
    scrim = Color.Black,
    surfaceContainerLowest = c(Palette.LightContainerLowest), surfaceContainerLow = c(Palette.LightContainerLow),
    surfaceContainer = c(Palette.LightContainer), surfaceContainerHigh = c(Palette.LightContainerHigh),
    surfaceContainerHighest = c(Palette.LightContainerHighest),
)

/** System sans, no font download. sp units scale with the user's font size. */
private val GitDripTypography = Typography(
    displaySmall = TextStyle(fontSize = 32.sp, lineHeight = 38.sp, fontWeight = FontWeight.SemiBold),
    headlineMedium = TextStyle(fontSize = 28.sp, lineHeight = 34.sp, fontWeight = FontWeight.SemiBold),
    headlineSmall = TextStyle(fontSize = 22.sp, lineHeight = 28.sp, fontWeight = FontWeight.SemiBold),
    titleLarge = TextStyle(fontSize = 20.sp, lineHeight = 26.sp, fontWeight = FontWeight.SemiBold),
    titleMedium = TextStyle(fontSize = 16.sp, lineHeight = 22.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.1.sp),
    titleSmall = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.1.sp),
    bodyLarge = TextStyle(fontSize = 16.sp, lineHeight = 24.sp, fontWeight = FontWeight.Normal),
    bodyMedium = TextStyle(fontSize = 15.sp, lineHeight = 22.sp, fontWeight = FontWeight.Normal),
    bodySmall = TextStyle(fontSize = 13.sp, lineHeight = 18.sp, fontWeight = FontWeight.Normal),
    labelLarge = TextStyle(fontSize = 14.sp, lineHeight = 20.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.1.sp),
    labelMedium = TextStyle(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.5.sp),
    labelSmall = TextStyle(fontSize = 11.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium, letterSpacing = 0.5.sp),
)

private val GitDripShapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(Radius.Control.dp),
    medium = RoundedCornerShape(Radius.Card.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(Radius.Sheet.dp),
)

@Composable
fun GitDripTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        typography = GitDripTypography,
        shapes = GitDripShapes,
        content = content,
    )
}
