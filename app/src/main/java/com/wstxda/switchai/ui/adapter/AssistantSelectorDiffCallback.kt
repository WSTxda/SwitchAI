package com.wstxda.switchai.ui.adapter

import androidx.recyclerview.widget.DiffUtil

class AssistantSelectorDiffCallback : DiffUtil.ItemCallback<AssistantSelectorRecyclerView>() {

    override fun areItemsTheSame(
        oldItem: AssistantSelectorRecyclerView, newItem: AssistantSelectorRecyclerView
    ): Boolean = when (oldItem) {
        is AssistantSelectorRecyclerView.AssistantSelector if newItem is AssistantSelectorRecyclerView.AssistantSelector -> oldItem.assistantItem.key == newItem.assistantItem.key
        is AssistantSelectorRecyclerView.CategoryHeader if newItem is AssistantSelectorRecyclerView.CategoryHeader -> oldItem.title == newItem.title
        is AssistantSelectorRecyclerView.ReorderTip if newItem is AssistantSelectorRecyclerView.ReorderTip -> true
        else -> false
    }

    override fun areContentsTheSame(
        oldItem: AssistantSelectorRecyclerView, newItem: AssistantSelectorRecyclerView
    ): Boolean = oldItem == newItem
}