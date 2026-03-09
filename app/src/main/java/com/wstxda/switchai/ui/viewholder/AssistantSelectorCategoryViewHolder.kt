package com.wstxda.switchai.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.wstxda.switchai.databinding.ListItemAssistantCategoryBinding
import com.wstxda.switchai.ui.adapter.AssistantSelectorRecyclerView

class AssistantSelectorCategoryViewHolder(
    private val binding: ListItemAssistantCategoryBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(categoryHeader: AssistantSelectorRecyclerView.CategoryHeader, isGridMode: Boolean = false) {
        binding.categoryTitleTextView.text = categoryHeader.title
        binding.categoryCountChip.text = categoryHeader.count.toString()
        val density = binding.root.resources.displayMetrics.density
        val startPadding = ((if (isGridMode) 8 else 40) * density).toInt()
        val endPadding = ((if (isGridMode) 8 else 28) * density).toInt()
        val topPadding = (12 * density).toInt()
        binding.root.setPadding(startPadding, topPadding, endPadding, 0)
    }
}