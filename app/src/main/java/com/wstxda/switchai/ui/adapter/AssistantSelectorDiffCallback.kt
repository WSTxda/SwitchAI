package com.wstxda.switchai.ui.adapter

import androidx.recyclerview.widget.DiffUtil

class AssistantSelectorDiffCallback(
    private val oldList: List<AssistantSelectorRecyclerView>,
    private val newList: List<AssistantSelectorRecyclerView>,
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean = when {
        oldList[oldPos] is AssistantSelectorRecyclerView.AssistantSelector && newList[newPos] is AssistantSelectorRecyclerView.AssistantSelector -> {
            val oldItem =
                (oldList[oldPos] as AssistantSelectorRecyclerView.AssistantSelector).assistantItem
            val newItem =
                (newList[newPos] as AssistantSelectorRecyclerView.AssistantSelector).assistantItem
            oldItem.key == newItem.key
        }

        oldList[oldPos] is AssistantSelectorRecyclerView.CategoryHeader && newList[newPos] is AssistantSelectorRecyclerView.CategoryHeader -> {
            val oldHeader = oldList[oldPos] as AssistantSelectorRecyclerView.CategoryHeader
            val newHeader = newList[newPos] as AssistantSelectorRecyclerView.CategoryHeader
            oldHeader.title == newHeader.title
        }

        else -> false
    }

    override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean =
        oldList[oldPos] == newList[newPos]
}