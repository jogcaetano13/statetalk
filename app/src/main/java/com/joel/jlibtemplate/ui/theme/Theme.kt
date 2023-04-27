package com.joel.jlibtemplate.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.joel.jtemplateapp.ui.theme.Shapes
import com.joel.jtemplateapp.ui.theme.Typography

val DarkColors = darkColors(
    primary = Color(0xFF000000),
    primaryVariant = Color(0xFF000000),
    background = Color(0xFF424242),
    surface = Color(0xFF000000),
    onBackground = Color.White,
    onSecondary = Color.White
)

val LightColors = lightColors(
    primary = Color(0xFFB83C4A),
    primaryVariant = Color(0xFF8F2F3A),
    background = Color(0xFFD8D6D6),
    surface = Color.White,
    onBackground = Color.Black,
    onSecondary = Color(0xFFB83C4A)
)

@Composable
fun CommunicationComposeTheme(
    content: @Composable () -> Unit
) {
    val darkTheme: Boolean = isSystemInDarkTheme()

    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}