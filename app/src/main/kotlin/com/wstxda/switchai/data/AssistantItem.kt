package com.wstxda.switchai.data

import com.wstxda.switchai.R
import kotlinx.serialization.Serializable

data class AssistantItem(
    val key: String,
    val name: String,
    val iconRes: Int,
    val isPinned: Boolean,
    val isInstalled: Boolean,
    val lastUsedTime: Long
)

enum class AssistantCategory(val titleRes: Int) {
    Pinned(R.string.selector_category_pin), Recent(R.string.selector_category_recent), All(R.string.selector_category_all), NotInstalled(
        R.string.selector_category_not_installed
    ),
}

sealed interface AssistantListItem {
    data class CategoryHeader(val category: AssistantCategory, val count: Int) : AssistantListItem
    data class AssistantSelector(val assistantItem: AssistantItem) : AssistantListItem
    data object ReorderTip : AssistantListItem
}

@Serializable
data class RecentAssistant(val key: String, val lastUsedTime: Long)