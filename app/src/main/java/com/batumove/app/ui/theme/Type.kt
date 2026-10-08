package com.batumove.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val BatuMoveTypography =
    Typography(

        displayLarge = TextStyle(
            fontSize = 42.sp,
            lineHeight = 46.sp,
            fontWeight = FontWeight.Bold,
        ),

        headlineLarge = TextStyle(
            fontSize = 30.sp,
            lineHeight = 36.sp,
            fontWeight = FontWeight.Bold,
        ),

        headlineMedium = TextStyle(
            fontSize = 24.sp,
            lineHeight = 30.sp,
            fontWeight = FontWeight.SemiBold,
        ),

        titleLarge = TextStyle(
            fontSize = 20.sp,
            lineHeight = 26.sp,
            fontWeight = FontWeight.SemiBold,
        ),

        bodyLarge = TextStyle(
            fontSize = 17.sp,
            lineHeight = 24.sp,
        ),

        bodyMedium = TextStyle(
            fontSize = 16.sp,
            lineHeight = 23.sp,
        ),

        bodySmall = TextStyle(
            fontSize = 14.sp,
            lineHeight = 20.sp,
        ),
    )