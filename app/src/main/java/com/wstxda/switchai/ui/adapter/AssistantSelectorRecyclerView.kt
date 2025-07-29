package com.wstxda.switchai.ui.adapter

import com.wstxda.switchai.data.AssistantItem

sealed interface AssistantSelectorRecyclerView {
    data class CategoryHeader(val title: String) : AssistantSelectorRecyclerView
    data class AssistantSelector(val assistantItem: AssistantItem) : AssistantSelectorRecyclerView
}