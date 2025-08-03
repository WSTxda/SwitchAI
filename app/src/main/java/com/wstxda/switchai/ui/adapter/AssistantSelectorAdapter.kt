package com.wstxda.switchai.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wstxda.switchai.databinding.ListItemAssistantCategoryBinding
import com.wstxda.switchai.databinding.ListItemAssistantViewBinding
import com.wstxda.switchai.ui.viewholder.AssistantSelectorCategoryViewHolder
import com.wstxda.switchai.ui.viewholder.AssistantSelectorItemViewHolder
import com.wstxda.switchai.utils.Constants

class AssistantSelectorAdapter(
    private val onAssistantLaunched: (String) -> Unit,
    private val onPinClicked: (String) -> Unit,
) : ListAdapter<AssistantSelectorRecyclerView, RecyclerView.ViewHolder>(
    AssistantSelectorDiffCallback()
) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is AssistantSelectorRecyclerView.CategoryHeader -> Constants.VIEW_TYPE_CATEGORY_HEADER
        is AssistantSelectorRecyclerView.AssistantSelector -> Constants.VIEW_TYPE_ASSISTANT_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        Constants.VIEW_TYPE_CATEGORY_HEADER -> {
            val binding = ListItemAssistantCategoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            AssistantSelectorCategoryViewHolder(binding)
        }

        Constants.VIEW_TYPE_ASSISTANT_ITEM -> {
            val binding = ListItemAssistantViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            AssistantSelectorItemViewHolder(binding)
        }

        else -> throw IllegalArgumentException("Unknown viewType $viewType")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        when (val item = getItem(pos)) {
            is AssistantSelectorRecyclerView.CategoryHeader -> (holder as AssistantSelectorCategoryViewHolder).bind(
                item
            )

            is AssistantSelectorRecyclerView.AssistantSelector -> (holder as AssistantSelectorItemViewHolder).bind(
                item, onAssistantLaunched, onPinClicked
            )
        }
    }
}