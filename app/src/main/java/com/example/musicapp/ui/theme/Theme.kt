package com.example.musicapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    background = GrayDarkTheme,
    onBackground = LightGrayDarkTheme,
    primary = BlackDarkTheme,
    secondary = LightBlackDarkTheme,
    tertiary = BlueDarkTheme,
    onTertiary = Blue50DarkTheme,
    surface = WhiteDarkTheme,
    onSurface = White70DarkTheme,
)

private val LightColorScheme = lightColorScheme(
    background = WhiteLightTheme,
    onBackground = GrayLightTheme,
    primary = DirtyWhiteLightTheme,
    secondary = GrayLightTheme,
    tertiary = BlueLightTheme,
    onTertiary = Blue50LightTheme,
    surface = BlackLightTheme,
    onSurface = Black70LightTheme,

    /* Other default colors to override
    primary
    secondary
    tertiary
    background ,
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onSurface = Color(0xFF1C1B1F),
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    */
)

@Composable
fun MusicAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) DarkColorScheme else LightColorScheme
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}