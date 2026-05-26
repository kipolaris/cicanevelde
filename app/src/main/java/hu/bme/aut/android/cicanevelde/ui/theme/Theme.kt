package hu.bme.aut.android.cicanevelde.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = HappyPink,
    onPrimary = WhitePink,

    secondary = OldPink,
    onSecondary = WhitePink,

    tertiary = ShimmerRojo,
    onTertiary = DarkText,

    background = PinkCrystal,
    onBackground = DarkText,

    surface = WhitePink,
    onSurface = DarkText,

    surfaceVariant = SoftPink,
    onSurfaceVariant = DarkText,

    error = Color(0xFFB00020),
    onError = WhitePink
)

@Composable
fun CicaneveldeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}