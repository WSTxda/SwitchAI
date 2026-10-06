package com.wstxda.switchai.ui.screens.preferences

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.wstxda.switchai.R
import com.wstxda.switchai.ui.components.preferences.DigitalAssistantCard
import com.wstxda.switchai.ui.components.preferences.PreferenceIcon
import com.wstxda.switchai.ui.components.preferences.SettingsCard
import com.wstxda.switchai.ui.components.preferences.SettingsPage
import com.wstxda.switchai.ui.navigation.Route
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Album
import top.yukonga.miuix.kmp.icon.extended.GridView
import top.yukonga.miuix.kmp.icon.extended.Help
import top.yukonga.miuix.kmp.icon.extended.Info
import top.yukonga.miuix.kmp.icon.extended.Mic
import top.yukonga.miuix.kmp.icon.extended.Settings
import top.yukonga.miuix.kmp.icon.extended.Theme
import top.yukonga.miuix.kmp.preference.ArrowPreference

@Composable
fun HomeScreen(
    assistant: String,
    setupDone: Boolean,
    onNavigate: (Route) -> Unit,
    onSetup: () -> Unit,
    onSelectAssistant: () -> Unit,
    onTutorial: () -> Unit,
) {
    SettingsPage(
        R.string.app_settings,
        actions = {
            IconButton(onClick = onTutorial) {
                Icon(MiuixIcons.Help, stringResource(R.string.digital_assistant_tutorial_title))
            }
        },
    ) {
        item("assistant") {
            DigitalAssistantCard(
                assistant,
                setupDone,
                if (setupDone) onSelectAssistant else onSetup,
            )
        }
        item("settings") {
            SettingsCard {
                ArrowPreference(
                    stringResource(R.string.pref_screen_appearance),
                    startAction = { PreferenceIcon(MiuixIcons.Demibold.Theme, Color(0xFF7667F8)) },
                    onClick = { onNavigate(Route.Appearance) },
                )
                ArrowPreference(
                    stringResource(R.string.pref_screen_selector),
                    startAction = { PreferenceIcon(MiuixIcons.Demibold.Album, Color(0xFF1DCD3A)) },
                    onClick = { onNavigate(Route.Selector) },
                )
                ArrowPreference(
                    stringResource(R.string.pref_screen_voice_input),
                    startAction = { PreferenceIcon(MiuixIcons.Demibold.Mic, Color(0xFFFA382E)) },
                    onClick = { onNavigate(Route.VoiceInput) },
                )
                ArrowPreference(
                    stringResource(R.string.pref_screen_accessibility),
                    startAction = {
                        PreferenceIcon(MiuixIcons.Demibold.Settings, Color(0xFF3482FF))
                    },
                    onClick = { onNavigate(Route.Accessibility) },
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ArrowPreference(
                        stringResource(R.string.pref_screen_shortcuts),
                        startAction = {
                            PreferenceIcon(MiuixIcons.Demibold.GridView, Color(0xFFFFBB10))
                        },
                        onClick = { onNavigate(Route.Shortcuts) },
                    )
                }
                ArrowPreference(
                    stringResource(R.string.pref_screen_about),
                    startAction = { PreferenceIcon(MiuixIcons.Demibold.Info, Color(0xFF8C9DAF)) },
                    onClick = { onNavigate(Route.About) },
                )
            }
        }
    }
}