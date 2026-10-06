package com.wstxda.switchai.ui.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wstxda.switchai.R
import com.wstxda.switchai.constants.AssistantsMap
import com.wstxda.switchai.ui.components.preferences.AssistantIcon
import com.wstxda.switchai.ui.components.preferences.OverlayCard
import com.wstxda.switchai.ui.components.preferences.overlayScrollModifiers
import com.wstxda.switchai.ui.utils.rememberAssistantResourcesManager
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.RadioButtonLocation
import top.yukonga.miuix.kmp.preference.RadioButtonPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun SupportedAssistantsDialog(show: Boolean, onOpen: (String) -> Unit, onDismiss: () -> Unit) {
    val resources = rememberAssistantResourcesManager()
    val locale = LocalConfiguration.current.locales.toLanguageTags()
    val assistants = remember(resources, locale) {
        AssistantsMap.assistantsVoiceInput.map { it to resources.getAssistantName(it) }
            .sortedBy { it.second.lowercase() }
    }
    OverlayDialog(
        show = show,
        title = stringResource(R.string.pref_voice_input_support),
        onDismissRequest = onDismiss,
    ) {
        Column {
            OverlayCard(Modifier.weight(1f, fill = false)) {
                LazyColumn(Modifier.overlayScrollModifiers()) {
                    items(assistants, key = { it.first }) { (key, name) ->
                        val icon = remember(resources, key) { resources.getAssistantIcon(key) }
                        ArrowPreference(
                            title = name,
                            startAction = {
                                AssistantIcon(
                                    icon,
                                    Modifier.padding(end = 4.dp),
                                    size = 24.dp,
                                )
                            },
                            onClick = {
                                onOpen(key)
                                onDismiss()
                            },
                        )
                    }
                }
            }
            TextButton(
                stringResource(android.R.string.cancel),
                onDismiss,
                Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            )
        }
    }
}

@Composable
fun AssistantChoiceDialog(
    show: Boolean,
    selected: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    val resources = rememberAssistantResourcesManager()
    val assistants = AssistantsMap.preferenceKeys
    val locale = LocalConfiguration.current.locales.toLanguageTags()
    OverlayDialog(
        show = show,
        title = stringResource(R.string.assistant_selector_title),
        onDismissRequest = onDismiss,
    ) {
        Column {
            OverlayCard(Modifier.weight(1f, fill = false)) {
                LazyColumn(Modifier.overlayScrollModifiers()) {
                    items(assistants, key = { it }) { key ->
                        val isSelected = key == selected
                        val name =
                            remember(resources, locale, key) { resources.getAssistantName(key) }
                        val icon = remember(resources, key) { resources.getAssistantIcon(key) }
                        RadioButtonPreference(
                            title = name,
                            selected = isSelected,
                            radioButtonLocation = RadioButtonLocation.End,
                            startAction = {
                                AssistantIcon(
                                    icon,
                                    tint = if (isSelected) MiuixTheme.colorScheme.primary
                                    else MiuixTheme.colorScheme.onSurface,
                                    size = 24.dp,
                                )
                            },
                            onClick = {
                                onSelect(key)
                                onDismiss()
                            },
                        )
                    }
                }
            }
            TextButton(
                stringResource(android.R.string.cancel),
                onDismiss,
                Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            )
        }
    }
}