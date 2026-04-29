package com.example.layceramictiles.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlueLight,
    onPrimary = TextWhite,
    secondary = PrimaryBlueLighter,
    onSecondary = TextWhite,
    tertiary = AccentOrange,
    onTertiary = TextWhite,
    background = SurfaceDark,
    onBackground = TextWhite,
    surface = SurfaceDarkElevated,
    onSurface = TextWhite,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = TextGrayLight,
    outline = BorderDark,
    error = ErrorRed,
    onError = OnErrorWhite
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = TextWhite,
    secondary = PrimaryBlueLight,
    onSecondary = TextWhite,
    tertiary = PrimaryBlueLighter,
    onTertiary = TextWhite,
    background = BackgroundLight,
    onBackground = TextDark,
    surface = SurfaceWhite,
    onSurface = TextDark,
    surfaceVariant = InputBackground,
    onSurfaceVariant = TextGray,
    outline = BorderLight,
    error = ErrorRed,
    onError = OnErrorWhite
)

@Composable
fun LayCeramicTilesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
