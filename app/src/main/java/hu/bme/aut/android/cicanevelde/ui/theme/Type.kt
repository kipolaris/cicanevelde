package hu.bme.aut.android.cicanevelde.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import hu.bme.aut.android.cicanevelde.R

val PixelSans = FontFamily(Font(R.font.fs_pixel_sans_unicode_regular))

val Typography = Typography(
    bodyLarge = TextStyle(fontFamily = PixelSans),
    bodyMedium = TextStyle(fontFamily = PixelSans),
    titleLarge = TextStyle(fontFamily = PixelSans),
    headlineLarge = TextStyle(fontFamily = PixelSans)
)