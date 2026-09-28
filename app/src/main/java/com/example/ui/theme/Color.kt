package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Instagram Signature Colors
val InstaBlue = Color(0xFF0095F6)
val InstaBlueDark = Color(0xFF1877F2)
val InstaHeart = Color(0xFFED4956)
val InstaBookmark = Color(0xFFFFB800)

// Instagram Story Gradient Palette
val GradientPurple = Color(0xFF833AB4)
val GradientMagenta = Color(0xFFC13584)
val GradientPink = Color(0xFFE1306C)
val GradientOrange = Color(0xFFF77737)
val GradientYellow = Color(0xFFFCAF45)

val StoryGradient = Brush.linearGradient(
    colors = listOf(
        GradientPurple,
        GradientMagenta,
        GradientPink,
        GradientOrange,
        GradientYellow
    )
)

val StorySeenGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF8E8E93),
        Color(0xFFC7C7CC)
    )
)

// Dark Theme Colors (Deep OLED / Black Instagram style)
val DarkBackground = Color(0xFF000000)
val DarkSurface = Color(0xFF121212)
val DarkSurfaceVariant = Color(0xFF1C1C1E)
val DarkBorder = Color(0xFF262626)
val DarkTextPrimary = Color(0xFFF5F5F7)
val DarkTextSecondary = Color(0xFFA1A1AA)

// Light Theme Colors
val LightBackground = Color(0xFFFFFFFF)
val LightSurface = Color(0xFFFAFAFA)
val LightSurfaceVariant = Color(0xFFF0F0F2)
val LightBorder = Color(0xFFE4E4E7)
val LightTextPrimary = Color(0xFF18181B)
val LightTextSecondary = Color(0xFF71717A)
