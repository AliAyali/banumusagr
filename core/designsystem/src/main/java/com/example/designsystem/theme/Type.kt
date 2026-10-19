package com.example.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import com.aliayali.designsystem.R

val YekanFontFamily = FontFamily(
    Font(
        resId = R.font.yekan_regular,
        weight = FontWeight.Normal,
    ),
    Font(
        resId = R.font.yekan_medium,
        weight = FontWeight.Medium,
    ),
    Font(
        resId = R.font.yekan_bold,
        weight = FontWeight.Bold,
    ),
)
internal val FfsTypography = Typography(

    displayLarge = TextStyle(
        fontFamily = YekanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = 0.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Right,
    ),

    displayMedium = TextStyle(
        fontFamily = YekanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Right,
    ),

    displaySmall = TextStyle(
        fontFamily = YekanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Right,
    ),

    headlineLarge = TextStyle(
        fontFamily = YekanFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Right,
    ),

    headlineMedium = TextStyle(
        fontFamily = YekanFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Right,
    ),

    headlineSmall = TextStyle(
        fontFamily = YekanFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Right,
    ),

    titleLarge = TextStyle(
        fontFamily = YekanFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Right,
    ),

    titleMedium = TextStyle(
        fontFamily = YekanFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Right,
    ),

    titleSmall = TextStyle(
        fontFamily = YekanFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Right,
    ),

    bodyLarge = TextStyle(
        fontFamily = YekanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Right,
    ),

    bodyMedium = TextStyle(
        fontFamily = YekanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Right,
    ),

    bodySmall = TextStyle(
        fontFamily = YekanFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Right,
    ),

    labelLarge = TextStyle(
        fontFamily = YekanFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Right,
    ),

    labelMedium = TextStyle(
        fontFamily = YekanFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Right,
    ),

    labelSmall = TextStyle(
        fontFamily = YekanFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        textDirection = TextDirection.Rtl,
        textAlign = TextAlign.Right,
    ),
)