package com.wstxda.switchai.ui.adapter

import com.wstxda.switchai.data.AssistantItem

sealed interface AssistantSelectorRecyclerView {

    data class CategoryHeader(val title: String, val count: Int) : AssistantSelectorRecyclerView
    data class AssistantSelector(val assistantItem: AssistantItem) : AssistantSelectorRecyclerView
    object ReorderTip : AssistantSelectorRecyclerView
}