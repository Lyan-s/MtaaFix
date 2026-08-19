package com.example.mtaafix.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * User's theme preference, set from Settings and persisted by the
 * caller (see MainActivity). SYSTEM follows the device's day/night
 * setting; LIGHT/DARK pin the app regardless of device setting.
 */
enum class ThemeMode {
    SYSTEM, LIGHT, DARK
}

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
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = OnLightSurfaceVariant,
    outline = OnLightSurfaceVariant.copy(alpha = 0.4f),
    outlineVariant = LightSurfaceVariant,
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
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = OnDarkSurfaceVariant,
    outline = OnDarkSurfaceVariant.copy(alpha = 0.4f),
    outlineVariant = DarkSurfaceVariant,
)

/**
 * MtaaFix app theme.
 *
 * @param dynamicColor When true (default), uses Android 12+ dynamic
 *   (wallpaper-based) color if available, falling back to the brand
 *   palette above. Set to false to always use the MtaaFix brand colors
 *   regardless of device/OS version — recommended if brand consistency
 *   matters more than matching the user's wallpaper.
 */
@Composable
fun MtaaFixTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
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

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}