package com.wstxda.switchai.ui.screens.preferences

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.wstxda.switchai.R
import com.wstxda.switchai.data.ShortcutBanner
import com.wstxda.switchai.ui.components.preferences.SettingsCard
import com.wstxda.switchai.ui.components.preferences.SettingsPage
import com.wstxda.switchai.ui.components.shortcuts.ShortcutBannerImage
import top.yukonga.miuix.kmp.preference.ArrowPreference

@Composable
fun ShortcutsScreen(onTile: () -> Unit, onWidget: () -> Unit, onBack: () -> Unit) {
    SettingsPage(R.string.pref_screen_shortcuts, onBack) {
        item("tile") {
            SettingsCard {
                ShortcutBannerImage(ShortcutBanner.Tile)
                ArrowPreference(
                    title = stringResource(R.string.pref_shortcut_tile),
                    summary = stringResource(R.string.pref_shortcut_tile_summary),
                    onClick = onTile,
                )
            }
        }
        item("widget") {
            SettingsCard {
                ShortcutBannerImage(ShortcutBanner.Widget)
                ArrowPreference(
                    title = stringResource(R.string.pref_shortcut_widget),
                    summary = stringResource(R.string.pref_shortcut_widget_summary),
                    onClick = onWidget,
                )
            }
        }
    }
}