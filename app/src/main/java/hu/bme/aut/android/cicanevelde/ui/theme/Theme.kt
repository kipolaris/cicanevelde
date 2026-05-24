package hu.bme.aut.android.cicanevelde.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = LavenderPurple,
    secondary = FrostedBlue,
    background = Periwinkle,
    surface = Mauve,
    onPrimary = Periwinkle,
    onSecondary = VelvetOrchid,
    onBackground = VelvetOrchid,
    onSurface = VelvetOrchid
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