package com.example.mtaafix.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = BluePrimary,
    onPrimary = OnBluePrimary,
    primaryContainer = BlueContainer,
    onPrimaryContainer = OnBlueContainer,
    secondary = OrangeSecondary,
    onSecondary = OnOrangeSecondary,
    secondaryContainer = OrangeContainer,
    onSecondaryContainer = OnOrangeContainer,
    tertiary = GreenTertiary,
    onTertiary = OnGreenTertiary,
    tertiaryContainer = GreenContainer,
    onTertiaryContainer = OnGreenContainer,
    background = LightBackground,
    onBackground = OnLightBackground,
    surface = LightBackground,
    onSurface = OnLightBackground,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = OnLightSurfaceVariant
)

private val DarkColors = darkColorScheme(
    primary = BluePrimaryDark,
    onPrimary = OnBluePrimaryDark,
    primaryContainer = BlueContainerDark,
    onPrimaryContainer = OnBlueContainerDark,
    secondary = OrangeSecondaryDark,
    onSecondary = OnOrangeSecondaryDark,
    secondaryContainer = OrangeContainerDark,
    onSecondaryContainer = OnOrangeContainerDark,
    tertiary = GreenTertiaryDark,
    onTertiary = OnGreenTertiaryDark,
    tertiaryContainer = GreenContainerDark,
    onTertiaryContainer = OnGreenContainerDark,
    background = DarkBackground,
    onBackground = OnDarkBackground,
    surface = DarkBackground,
    onSurface = OnDarkBackground,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = OnDarkSurfaceVariant
)

@Composable
fun MtaaFixTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color pulls the palette from the user's wallpaper on Android 12+.
    // Turned off here so MtaaFix always shows its own terracotta/teal branding
    // instead of a color scheme that changes per device.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}