package com.wstxda.switchai.ui.adapter

import androidx.recyclerview.widget.DiffUtil

class AssistantSelectorDiffCallback : DiffUtil.ItemCallback<AssistantSelectorRecyclerView>() {
    override fun areItemsTheSame(
        oldItem: AssistantSelectorRecyclerView, newItem: AssistantSelectorRecyclerView
    ): Boolean = when {
        oldItem is AssistantSelectorRecyclerView.AssistantSelector && newItem is AssistantSelectorRecyclerView.AssistantSelector -> oldItem.assistantItem.key == newItem.assistantItem.key

        oldItem is AssistantSelectorRecyclerView.CategoryHeader && newItem is AssistantSelectorRecyclerView.CategoryHeader -> oldItem.title == newItem.title

        else -> false
    }

    override fun areContentsTheSame(
        oldItem: AssistantSelectorRecyclerView, newItem: AssistantSelectorRecyclerView
    ): Boolean = oldItem == newItem
}