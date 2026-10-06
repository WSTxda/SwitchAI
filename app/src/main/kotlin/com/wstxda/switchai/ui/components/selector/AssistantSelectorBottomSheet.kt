package com.wstxda.switchai.ui.components.selector

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.wstxda.switchai.R
import com.wstxda.switchai.constants.Constants
import com.wstxda.switchai.data.AssistantListItem
import com.wstxda.switchai.data.SelectorState
import com.wstxda.switchai.data.SettingsState
import com.wstxda.switchai.ui.components.preferences.bottomSheetContentInsets
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.ScrollMoveMode
import sh.calvin.reorderable.rememberReorderableLazyGridState
import top.yukonga.miuix.kmp.basic.CircularProgressIndicator
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.utils.scrollEndHaptic
import top.yukonga.miuix.kmp.window.WindowBottomSheet

@Composable
fun AssistantSelectorBottomSheet(
    show: Boolean,
    settings: SettingsState,
    state: SelectorState,
    onSearch: (String) -> Unit,
    onPin: (String) -> Unit,
    onMovePinned: (String, String) -> Unit,
    onReorderFinished: () -> Unit,
    onDismissTip: () -> Unit,
    onAssistant: (String) -> Unit,
    onDismiss: () -> Unit,
    onDismissFinished: () -> Unit,
) {
    WindowBottomSheet(
        show = show,
        insideMargin = DpSize.Zero,
        title = if (Constants.SELECTOR_COMPONENT_SELECTOR_TITLE in settings.components) stringResource(
            R.string.assistant_selector_title
        )
        else null,
        onDismissRequest = onDismiss,
        onDismissFinished = onDismissFinished,
    ) {
        val columns =
            if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) settings.landscapeColumns
            else settings.portraitColumns
        val haptics = LocalHapticFeedback.current
        val grid = rememberLazyGridState()
        val pinned = remember(state.items) {
            state.items.filterIsInstance<AssistantListItem.AssistantSelector>()
                .map { it.assistantItem }.filter { it.isPinned && it.isInstalled }
        }
        val pinnedIndices =
            remember(pinned) { pinned.mapIndexed { index, item -> item.key to index }.toMap() }
        val reorder = rememberReorderableLazyGridState(
            lazyGridState = grid,
            scrollMoveMode = ScrollMoveMode.INSERT,
        ) { from, to ->
            if (from.key in pinnedIndices && to.key in pinnedIndices) onMovePinned(
                from.key as String, to.key as String
            )
        }
        val before = stringResource(R.string.selector_move_before)
        val after = stringResource(R.string.selector_move_after)
        Column(
            Modifier
                .fillMaxWidth()
                .bottomSheetContentInsets()
        ) {
            Box(Modifier.padding(horizontal = 24.dp)) {
                SelectorHeader(
                    Constants.SELECTOR_COMPONENT_SEARCH_BAR in settings.components,
                    state.query,
                    onSearch,
                )
            }
            if (state.loading || state.searchEmpty || state.loadError) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(24.dp), contentAlignment = Alignment.Center
                ) {
                    when {
                        state.loadError -> Text(stringResource(R.string.settings_storage_error))
                        state.loading -> CircularProgressIndicator()
                        else -> Text(stringResource(R.string.selector_search_empty))
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columns),
                    state = grid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false)
                        .then(if (settings.vibration) Modifier.scrollEndHaptic() else Modifier)
                        .overScrollVertical(),
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(
                        items = state.items,
                        key = { item ->
                            when (item) {
                                is AssistantListItem.AssistantSelector -> item.assistantItem.key
                                is AssistantListItem.CategoryHeader -> "category:${item.category.name}"

                                AssistantListItem.ReorderTip -> "reorderTip"
                            }
                        },
                        contentType = { item ->
                            when (item) {
                                is AssistantListItem.AssistantSelector -> "assistant"
                                is AssistantListItem.CategoryHeader -> "category"
                                AssistantListItem.ReorderTip -> "tip"
                            }
                        },
                        span = { item ->
                            GridItemSpan(
                                if (item is AssistantListItem.AssistantSelector) 1 else maxLineSpan
                            )
                        },
                    ) { item ->
                        when (item) {
                            is AssistantListItem.CategoryHeader -> SelectorCategory(
                                item,
                                Constants.SELECTOR_COMPONENT_COUNTER in settings.components,
                                Modifier.animateItem(),
                            )

                            AssistantListItem.ReorderTip -> SelectorReorderTip(
                                Modifier
                                    .animateItem()
                                    .padding(vertical = 8.dp)
                            ) {
                                onDismissTip()
                                if (settings.vibration) haptics.performHapticFeedback(
                                    HapticFeedbackType.ToggleOff
                                )
                            }

                            is AssistantListItem.AssistantSelector -> {
                                val assistant = item.assistantItem
                                val index = pinnedIndices[assistant.key]
                                val canReorder = state.query.isBlank() && index != null
                                ReorderableItem(
                                    state = reorder,
                                    key = assistant.key,
                                    enabled = canReorder,
                                ) { _ ->
                                    AssistantCard(
                                        assistant,
                                        columns,
                                        modifier = Modifier
                                            .longPressDraggableHandle(
                                                enabled = canReorder,
                                                onDragStarted = {
                                                    if (settings.vibration) haptics.performHapticFeedback(
                                                        HapticFeedbackType.LongPress
                                                    )
                                                },
                                                onDragStopped = onReorderFinished,
                                            )
                                            .semantics {
                                                customActions = buildList {
                                                    if (canReorder && index > 0) add(
                                                        CustomAccessibilityAction(
                                                            before
                                                        ) {
                                                            onMovePinned(
                                                                assistant.key,
                                                                pinned[index - 1].key,
                                                            )
                                                            onReorderFinished()
                                                            true
                                                        })
                                                    if (canReorder && index < pinned.lastIndex) add(
                                                        CustomAccessibilityAction(
                                                            after
                                                        ) {
                                                            onMovePinned(
                                                                assistant.key,
                                                                pinned[index + 1].key,
                                                            )
                                                            onReorderFinished()
                                                            true
                                                        })
                                                }
                                            },
                                        onClick = { onAssistant(assistant.key) },
                                        onPin = {
                                            onPin(assistant.key)
                                            if (settings.vibration) haptics.performHapticFeedback(
                                                if (assistant.isPinned) HapticFeedbackType.ToggleOff
                                                else HapticFeedbackType.ToggleOn
                                            )
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}