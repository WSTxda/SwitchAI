package com.wstxda.switchai.data

import com.wstxda.switchai.constants.Constants
import top.yukonga.miuix.kmp.theme.ThemeColorSpec
import top.yukonga.miuix.kmp.theme.ThemePaletteStyle

enum class ThemeMode(val storedValue: String) { System("system"), Light("light"), Dark("dark") }
enum class TopBarBlurStyle(val value: Int) { Gaussian(0), Progressive(1) }

data class AppearanceSettings(
    val theme: ThemeMode = Constants.DEFAULT_THEME,
    val monet: Boolean = Constants.DEFAULT_MONET,
    val keyColor: Int = Constants.DEFAULT_KEY_COLOR,
    val paletteStyle: ThemePaletteStyle = Constants.DEFAULT_PALETTE_STYLE,
    val colorSpec: ThemeColorSpec = Constants.DEFAULT_COLOR_SPEC,
    val blur: Boolean = Constants.DEFAULT_BLUR,
    val topBarBlurStyle: TopBarBlurStyle = Constants.DEFAULT_TOP_BAR_BLUR_STYLE,
)