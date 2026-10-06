package com.wstxda.switchai.logic

import com.wstxda.switchai.data.AssistantCategory
import com.wstxda.switchai.data.AssistantItem
import com.wstxda.switchai.data.AssistantListItem
import com.wstxda.switchai.data.SettingsState

internal fun buildAssistantCategories(
    available: List<AssistantItem>,
    sortedByName: List<AssistantItem>,
    settings: SettingsState,
): List<AssistantListItem> {
    val pinned = settings.pinnedAssistants.toSet()
    val recent = settings.recentlyUsedAssistants.associate { it.key to it.lastUsedTime }
    val assistants = available.map {
        val isPinned = it.key in pinned
        val lastUsedTime = recent[it.key] ?: 0L
        if (it.isPinned == isPinned && it.lastUsedTime == lastUsedTime) it
        else it.copy(isPinned = isPinned, lastUsedTime = lastUsedTime)
    }
    val byKey = assistants.associateBy { it.key }
    val pins =
        settings.pinnedAssistants.mapNotNull { byKey[it]?.takeIf { item -> item.isInstalled } }
    val recentItems = assistants.filter { it.isInstalled && !it.isPinned && it.key in recent }
    val alphabetical = sortedByName.map { byKey.getValue(it.key) }
    val others = alphabetical.filter { it.isInstalled && !it.isPinned && it.key !in recent }
    val notInstalled = alphabetical.filterNot { it.isInstalled }
    val categorized = buildList {
        fun category(category: AssistantCategory, entries: List<AssistantItem>) {
            if (entries.isEmpty()) return
            add(AssistantListItem.CategoryHeader(category, entries.size))
            if (category == AssistantCategory.Pinned && entries.size >= 2 && !settings.reorderTipDismissed) add(
                AssistantListItem.ReorderTip
            )
            addAll(entries.map { AssistantListItem.AssistantSelector(it) })
        }
        category(AssistantCategory.Pinned, pins)
        category(AssistantCategory.Recent, recentItems.sortedByDescending { it.lastUsedTime })
        category(AssistantCategory.All, others)
        category(AssistantCategory.NotInstalled, notInstalled)
    }
    return categorized
}

internal fun filterAssistants(
    master: List<AssistantListItem>,
    query: String,
): List<AssistantListItem> = if (query.isBlank()) master
else master.filter {
    it is AssistantListItem.AssistantSelector && it.assistantItem.name.contains(
        query, ignoreCase = true
    )
}

internal fun sortAssistantsByName(assistants: List<AssistantItem>): List<AssistantItem> =
    assistants.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it: AssistantItem -> it.name })

internal fun reorderPinnedSection(
    master: List<AssistantListItem>,
    order: List<String>,
): List<AssistantListItem> {
    val header = master.indexOfFirst {
        it is AssistantListItem.CategoryHeader && it.category == AssistantCategory.Pinned
    }
    if (header < 0) return master
    var start = header + 1
    if (master.getOrNull(start) == AssistantListItem.ReorderTip) start++
    var end = start
    while (master.getOrNull(end) is AssistantListItem.AssistantSelector) end++
    val pins = master.subList(start, end).filterIsInstance<AssistantListItem.AssistantSelector>()
        .associateBy { it.assistantItem.key }
    val keys = reorderPinnedAssistants(pins.keys.toList(), order)
    if (keys == pins.keys.toList()) return master
    return master.toMutableList().apply {
        keys.forEachIndexed { index, key -> this[start + index] = pins.getValue(key) }
    }
}

internal fun reorderPinnedAssistants(current: List<String>, requested: List<String>): List<String> {
    val reordered = requested.distinct().filter { it in current }
    return reordered + current.filterNot { it in reordered }
}