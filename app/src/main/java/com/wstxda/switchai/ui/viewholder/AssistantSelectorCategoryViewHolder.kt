package com.wstxda.switchai.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.wstxda.switchai.databinding.ListItemAssistantCategoryBinding
import com.wstxda.switchai.ui.adapter.AssistantSelectorRecyclerView

class AssistantSelectorCategoryViewHolder(
    private val binding: ListItemAssistantCategoryBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(categoryHeader: AssistantSelectorRecyclerView.CategoryHeader) {
        binding.categoryTitleTextView.text = categoryHeader.title
    }
}