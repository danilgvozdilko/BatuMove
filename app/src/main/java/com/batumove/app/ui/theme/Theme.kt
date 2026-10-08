package com.batumove.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.batumove.app.domain.model.AppTheme


private val BatuMoveLightColors =
    lightColorScheme(
        primary = GeorgianRed,
        onPrimary = BackgroundPrimary,

        primaryContainer = GeorgianRedLight,
        onPrimaryContainer = TextPrimary,

        background = BackgroundPrimary,
        onBackground = TextPrimary,

        surface = SurfacePrimary,
        onSurface = TextPrimary,

        surfaceVariant = SurfaceSecondary,
        onSurfaceVariant = TextSecondary,

        error = ErrorRed,
    )

private val BatuMoveDarkColors =
    darkColorScheme(
        primary = GeorgianRed,
        onPrimary = Color.White,

        primaryContainer = GeorgianRed,
        onPrimaryContainer = Color.White,

        background = Color(0xFF121212),
        onBackground = Color.White,

        surface = Color(0xFF1E1E1E),
        onSurface = Color.White,

        surfaceVariant = Color(0xFF2A2A2A),
        onSurfaceVariant = Color(0xFFBDBDBD),

        error = ErrorRed,
    )

@Composable
fun BatuMoveTheme(
    theme: AppTheme,
    content: @Composable () -> Unit,
) {
    val colorScheme = when (theme) {
        AppTheme.LIGHT -> BatuMoveLightColors
        AppTheme.DARK -> BatuMoveDarkColors
        else -> {
            BatuMoveLightColors
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = BatuMoveTypography,
        content = content,
    )
}