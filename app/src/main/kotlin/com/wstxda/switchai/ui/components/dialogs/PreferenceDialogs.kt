package com.wstxda.switchai.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wstxda.switchai.R
import com.wstxda.switchai.constants.Constants
import com.wstxda.switchai.ui.components.preferences.AssistantIcon
import com.wstxda.switchai.ui.components.preferences.OverlayCard
import com.wstxda.switchai.ui.components.preferences.overlayScrollModifiers
import com.wstxda.switchai.ui.utils.rememberAssistantResourcesManager
import top.yukonga.miuix.kmp.basic.ButtonDefaults
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.TextButton
import top.yukonga.miuix.kmp.overlay.OverlayDialog
import top.yukonga.miuix.kmp.preference.CheckboxLocation
import top.yukonga.miuix.kmp.preference.CheckboxPreference
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun MultiChoiceDialog(
    show: Boolean,
    title: String,
    entries: List<String>,
    values: List<String>,
    initial: Set<String>,
    minimum: Int = 0,
    assistantIcons: Boolean = false,
    onSave: (Set<String>) -> Unit,
    onDismiss: () -> Unit,
) {
    val resources = rememberAssistantResourcesManager()
    OverlayDialog(show = show, title = title, onDismissRequest = onDismiss) {
        var selected by rememberSaveable { mutableStateOf(initial.toList()) }
        Column {
            OverlayCard(Modifier.weight(1f, fill = false)) {
                LazyColumn(Modifier.overlayScrollModifiers()) {
                    itemsIndexed(values, key = { _, key -> key }) { index, value ->
                        val icon = if (assistantIcons) remember(resources, value) {
                            resources.getAssistantIcon(value)
                        } else null
                        CheckboxPreference(
                            title = entries[index],
                            checkboxLocation = CheckboxLocation.End,
                            startAction = if (icon != null) {
                                {
                                    AssistantIcon(
                                        icon,
                                        size = 24.dp,
                                    )
                                }
                            } else null,
                            checked = value in selected,
                            onCheckedChange = { checked ->
                                selected =
                                    if (checked) (selected + value).distinct() else selected - value
                            },
                        )
                    }
                }
            }
            if (selected.size < minimum) {
                Text(
                    pluralStringResource(R.plurals.error_min_selection, minimum, minimum),
                    color = MiuixTheme.colorScheme.error,
                )
            }
            ConfirmButtons(
                onDismiss,
                {
                    onSave(selected.toSet())
                    onDismiss()
                },
                enabled = selected.size >= minimum,
            )
        }
    }
}

@Composable
fun ConfirmButtons(
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    confirmText: String = stringResource(android.R.string.ok),
    enabled: Boolean = true,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        TextButton(stringResource(android.R.string.cancel), onCancel, Modifier.weight(1f))
        TextButton(
            confirmText,
            onConfirm,
            Modifier.weight(1f),
            enabled = enabled,
            colors = ButtonDefaults.textButtonColorsPrimary(),
        )
    }
}

@Composable
fun GridDialog(
    show: Boolean,
    portraitColumns: Int,
    landscapeColumns: Int,
    onSave: (Int, Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val previewColor = MiuixTheme.colorScheme.tertiaryContainer
    OverlayDialog(
        show = show,
        title = stringResource(R.string.selector_grid_title),
        onDismissRequest = onDismiss,
    ) {
        var portrait by rememberSaveable { mutableIntStateOf(portraitColumns) }
        var landscape by rememberSaveable { mutableIntStateOf(landscapeColumns) }
        var orientation by rememberSaveable { mutableIntStateOf(0) }
        val columns = if (orientation == 0) portrait else landscape
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OverlayCard {
                OverlayDropdownPreference(
                    title = stringResource(R.string.selector_grid_orientation),
                    items = listOf(
                        stringResource(R.string.selector_grid_portrait),
                        stringResource(R.string.selector_grid_landscape),
                    ),
                    selectedIndex = orientation,
                    onSelectedIndexChange = { orientation = it },
                )
                OverlayDropdownPreference(
                    title = stringResource(R.string.selector_grid_columns),
                    items = (1..Constants.MAX_GRID_COLUMNS).map(Int::toString),
                    selectedIndex = columns - 1,
                    onSelectedIndexChange = {
                        if (orientation == 0) portrait = it + 1 else landscape = it + 1
                    },
                )
            }
            repeat(Constants.GRID_PREVIEW_ROW_COUNT) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(columns) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            cornerRadius = 12.dp,
                            colors = CardDefaults.defaultColors(previewColor),
                        ) {}
                    }
                }
            }
            ConfirmButtons(
                onDismiss,
                {
                    onSave(portrait, landscape)
                    onDismiss()
                },
            )
        }
    }
}