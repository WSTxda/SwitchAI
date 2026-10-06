package com.wstxda.switchai.ui.screens.preferences

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.wstxda.switchai.R
import com.wstxda.switchai.ui.components.preferences.SettingsCard
import com.wstxda.switchai.ui.components.preferences.SettingsPage
import com.wstxda.switchai.ui.components.preferences.SupportingText
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference

@Composable
fun VoiceInputScreen(
    voiceInput: Boolean,
    shizukuVoiceInput: Boolean,
    onVoiceInputChange: (Boolean) -> Unit,
    onShizukuVoiceInputChange: (Boolean) -> Unit,
    onSupportedAssistants: () -> Unit,
    onBack: () -> Unit,
) {
    SettingsPage(R.string.pref_screen_voice_input, onBack) {
        item {
            SettingsCard {
                SwitchPreference(
                    title = stringResource(R.string.pref_voice_input),
                    checked = voiceInput,
                    onCheckedChange = onVoiceInputChange,
                )
            }
        }
        item {
            SettingsCard {
                ArrowPreference(
                    title = stringResource(R.string.pref_voice_input_support),
                    enabled = voiceInput,
                    onClick = onSupportedAssistants,
                )
            }
        }
        item { SmallTitle(stringResource(R.string.pref_category_advanced)) }
        item {
            SettingsCard {
                SwitchPreference(
                    title = stringResource(R.string.pref_voice_input_shizuku),
                    summary = stringResource(R.string.pref_voice_input_shizuku_summary),
                    checked = shizukuVoiceInput,
                    enabled = voiceInput,
                    onCheckedChange = onShizukuVoiceInputChange,
                )
            }
        }
        item { SupportingText(R.string.pref_voice_input_summary) }
    }
}