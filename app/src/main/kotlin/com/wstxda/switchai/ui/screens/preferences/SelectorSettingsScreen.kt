package com.wstxda.switchai.ui.screens.preferences

import androidx.compose.animation.AnimatedVisibility
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
fun SelectorSettingsScreen(
    selectorEnabled: Boolean,
    dynamicManager: Boolean,
    onSelectorChange: (Boolean) -> Unit,
    onDynamicManagerChange: (Boolean) -> Unit,
    onComponents: () -> Unit,
    onAssistants: () -> Unit,
    onGrid: () -> Unit,
    onBack: () -> Unit,
) {
    SettingsPage(R.string.pref_screen_selector, onBack) {
        item {
            SettingsCard {
                SwitchPreference(
                    title = stringResource(R.string.pref_selector),
                    checked = selectorEnabled,
                    onCheckedChange = onSelectorChange,
                )
            }
        }
        item { SmallTitle(stringResource(R.string.pref_category_manager)) }
        item {
            SettingsCard {
                ArrowPreference(
                    title = stringResource(R.string.pref_selector_manager_components),
                    summary = stringResource(R.string.pref_selector_manager_components_summary),
                    enabled = selectorEnabled,
                    onClick = onComponents,
                )
                SwitchPreference(
                    title = stringResource(R.string.pref_selector_manager_dynamic),
                    summary = stringResource(
                        if (dynamicManager) R.string.pref_selector_manager_dynamic_summary_on
                        else R.string.pref_selector_manager_dynamic_summary_off
                    ),
                    checked = dynamicManager,
                    enabled = selectorEnabled,
                    onCheckedChange = onDynamicManagerChange,
                )
                AnimatedVisibility(!dynamicManager) {
                    ArrowPreference(
                        title = stringResource(R.string.pref_selector_manager_manual),
                        summary = stringResource(R.string.pref_selector_manager_manual_summary),
                        enabled = selectorEnabled,
                        onClick = onAssistants,
                    )
                }
            }
        }
        item { SmallTitle(stringResource(R.string.pref_category_appearance)) }
        item {
            SettingsCard {
                ArrowPreference(
                    title = stringResource(R.string.pref_selector_grid),
                    summary = stringResource(R.string.pref_selector_grid_summary),
                    enabled = selectorEnabled,
                    onClick = onGrid,
                )
            }
        }
        item { SupportingText(R.string.pref_selector_summary) }
    }
}