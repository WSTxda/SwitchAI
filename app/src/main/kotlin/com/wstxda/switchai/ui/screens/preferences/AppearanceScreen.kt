package com.wstxda.switchai.ui.screens.preferences

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.wstxda.switchai.R
import com.wstxda.switchai.constants.Constants
import com.wstxda.switchai.data.AppearanceSettings
import com.wstxda.switchai.data.ThemeMode
import com.wstxda.switchai.data.TopBarBlurStyle
import com.wstxda.switchai.ui.components.preferences.SettingsCard
import com.wstxda.switchai.ui.components.preferences.SettingsPage
import top.yukonga.miuix.kmp.basic.DropdownItem
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.blur.isRuntimeShaderSupported
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.preference.OverlaySpinnerPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeColorSpec
import top.yukonga.miuix.kmp.theme.ThemePaletteStyle

@Composable
fun AppearanceScreen(
    appearance: AppearanceSettings,
    onThemeChange: (ThemeMode) -> Unit,
    onMonetChange: (Boolean) -> Unit,
    onKeyColorChange: (Int) -> Unit,
    onPaletteStyleChange: (ThemePaletteStyle) -> Unit,
    onColorSpecChange: (ThemeColorSpec) -> Unit,
    onBlurChange: (Boolean) -> Unit,
    onTopBarBlurStyleChange: (TopBarBlurStyle) -> Unit,
    onBack: () -> Unit,
    blurSupported: Boolean = isRuntimeShaderSupported(),
) {
    val entries = stringArrayResource(R.array.theme_entries).toList()
    val seeds = stringArrayResource(R.array.theme_key_color_entries).toList()
    val swatchPainter = remember { RoundedRectanglePainter() }
    val defaultColor = MiuixTheme.colorScheme.primary
    val seedItems = seeds.mapIndexed { index, name ->
        DropdownItem(
            text = name,
            icon = { modifier ->
                Box(modifier, contentAlignment = Alignment.Center) {
                    Icon(
                        painter = swatchPainter,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Constants.KEY_COLORS.getOrNull(index - 1) ?: defaultColor,
                    )
                }
            },
        )
    }
    val palettes = ThemePaletteStyle.entries.map { it.name }
    val specLabels = ThemeColorSpec.entries.map {
        stringResource(
            when (it) {
                ThemeColorSpec.Spec2025 -> R.string.theme_spec_2025
                ThemeColorSpec.Spec2021 -> R.string.theme_spec_2021
            }
        )
    }
    SettingsPage(R.string.pref_screen_appearance, onBack) {
        item("theme") {
            SettingsCard {
                OverlayDropdownPreference(
                    title = stringResource(R.string.pref_theme),
                    items = entries,
                    selectedIndex = appearance.theme.ordinal,
                    onSelectedIndexChange = { onThemeChange(ThemeMode.entries[it]) },
                )
                SwitchPreference(
                    title = stringResource(R.string.theme_monet),
                    checked = appearance.monet,
                    onCheckedChange = onMonetChange,
                )
                AnimatedVisibility(appearance.monet) {
                    Column {
                        OverlaySpinnerPreference(
                            title = stringResource(R.string.pref_theme_key_color),
                            items = seedItems,
                            selectedIndex = appearance.keyColor,
                            onSelectedIndexChange = onKeyColorChange,
                        )
                        AnimatedVisibility(appearance.keyColor > 0) {
                            Column {
                                OverlayDropdownPreference(
                                    title = stringResource(R.string.pref_theme_palette_style),
                                    items = palettes,
                                    selectedIndex = appearance.paletteStyle.ordinal,
                                    onSelectedIndexChange = {
                                        onPaletteStyleChange(ThemePaletteStyle.entries[it])
                                    },
                                )
                                OverlayDropdownPreference(
                                    title = stringResource(R.string.pref_theme_color_spec),
                                    items = specLabels,
                                    selectedIndex = appearance.colorSpec.ordinal,
                                    onSelectedIndexChange = {
                                        onColorSpecChange(ThemeColorSpec.entries[it])
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
        if (blurSupported) item("blur") {
            SettingsCard {
                SwitchPreference(
                    title = stringResource(R.string.pref_theme_blur),
                    checked = appearance.blur,
                    onCheckedChange = onBlurChange,
                )
                AnimatedVisibility(appearance.blur) {
                    OverlayDropdownPreference(
                        title = stringResource(R.string.pref_topbar_blur_style),
                        items = listOf(
                            stringResource(R.string.topbar_blur_gaussian),
                            stringResource(R.string.topbar_blur_progressive),
                        ),
                        selectedIndex = appearance.topBarBlurStyle.value,
                        onSelectedIndexChange = {
                            onTopBarBlurStyleChange(TopBarBlurStyle.entries[it])
                        },
                    )
                }
            }
        }
    }
}

private class RoundedRectanglePainter(private val cornerRadius: Dp = 6.dp) : Painter() {
    override val intrinsicSize = Size.Unspecified

    override fun DrawScope.onDraw() {
        drawRoundRect(
            color = Color.White,
            size = size,
            cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx()),
        )
    }
}