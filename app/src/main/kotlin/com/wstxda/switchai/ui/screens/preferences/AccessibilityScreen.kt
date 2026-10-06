package com.wstxda.switchai.ui.screens.preferences

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.wstxda.switchai.R
import com.wstxda.switchai.ui.components.preferences.SettingsCard
import com.wstxda.switchai.ui.components.preferences.SettingsPage
import top.yukonga.miuix.kmp.preference.SwitchPreference

@Composable
fun AccessibilityScreen(
    vibration: Boolean,
    sound: Boolean,
    onVibrationChange: (Boolean) -> Unit,
    onSoundChange: (Boolean) -> Unit,
    onBack: () -> Unit,
) {
    SettingsPage(R.string.pref_screen_accessibility, onBack) {
        item {
            SettingsCard {
                SwitchPreference(
                    title = stringResource(R.string.pref_accessibility_vibration),
                    summary = stringResource(R.string.pref_accessibility_vibration_summary),
                    checked = vibration,
                    onCheckedChange = onVibrationChange,
                )
                SwitchPreference(
                    title = stringResource(R.string.pref_accessibility_sound),
                    summary = stringResource(R.string.pref_accessibility_sound_summary),
                    checked = sound,
                    onCheckedChange = onSoundChange,
                )
            }
        }
    }
}