package com.wstxda.switchai.ui.theme

import android.graphics.Color as AndroidColor
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.wstxda.switchai.constants.Constants
import com.wstxda.switchai.data.AppearanceSettings
import com.wstxda.switchai.data.ThemeMode
import top.yukonga.miuix.kmp.theme.ColorSchemeMode
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeController

val LocalAppearance = staticCompositionLocalOf { AppearanceSettings() }
val LocalDarkTheme = staticCompositionLocalOf { false }

@Composable
fun SwitchAITheme(appearance: AppearanceSettings, content: @Composable () -> Unit) {
    val dark = when (appearance.theme) {
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
        ThemeMode.System -> isSystemInDarkTheme()
    }
    val mode = when (appearance.theme) {
        ThemeMode.Light -> if (appearance.monet) ColorSchemeMode.MonetLight else ColorSchemeMode.Light

        ThemeMode.Dark -> if (appearance.monet) ColorSchemeMode.MonetDark else ColorSchemeMode.Dark

        ThemeMode.System -> if (appearance.monet) ColorSchemeMode.MonetSystem else ColorSchemeMode.System
    }
    val seed = Constants.KEY_COLORS.getOrNull(appearance.keyColor - 1)
    val palette = appearance.paletteStyle
    val spec = appearance.colorSpec
    val controller = remember(mode, seed, palette, spec) {
        ThemeController(mode, keyColor = seed, paletteStyle = palette, colorSpec = spec)
    }
    val activity = LocalActivity.current as? ComponentActivity
    DisposableEffect(activity, dark) {
        activity?.apply {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(
                    AndroidColor.TRANSPARENT, AndroidColor.TRANSPARENT
                ) {
                    dark
                },
                navigationBarStyle = SystemBarStyle.auto(
                    AndroidColor.TRANSPARENT, AndroidColor.TRANSPARENT
                ) {
                    dark
                },
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) window.isNavigationBarContrastEnforced =
                false
        }
        onDispose {}
    }
    CompositionLocalProvider(LocalAppearance provides appearance, LocalDarkTheme provides dark) {
        MiuixTheme(controller = controller, content = content)
    }
}