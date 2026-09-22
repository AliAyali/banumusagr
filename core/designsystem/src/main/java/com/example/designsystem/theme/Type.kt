package com.example.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.aliayali.designsystem.R

val YekanFontFamily = FontFamily(
    Font(
        R.font.yekan_regular,
        FontWeight.Normal,
    ),
    Font(
        R.font.yekan_medium,
        FontWeight.Medium,
    ),
    Font(
        R.font.yekan_bold,
        FontWeight.Bold,
    ),
)

val BanumusaTypography = Typography().run {
    copy(
        bodyLarge = bodyLarge.copy(fontFamily = YekanFontFamily),
        bodyMedium = bodyMedium.copy(fontFamily = YekanFontFamily),
        bodySmall = bodySmall.copy(fontFamily = YekanFontFamily),

        titleLarge = titleLarge.copy(fontFamily = YekanFontFamily),
        titleMedium = titleMedium.copy(fontFamily = YekanFontFamily),
        titleSmall = titleSmall.copy(fontFamily = YekanFontFamily),

        labelLarge = labelLarge.copy(fontFamily = YekanFontFamily),
        labelMedium = labelMedium.copy(fontFamily = YekanFontFamily),
        labelSmall = labelSmall.copy(fontFamily = YekanFontFamily),
    )
}