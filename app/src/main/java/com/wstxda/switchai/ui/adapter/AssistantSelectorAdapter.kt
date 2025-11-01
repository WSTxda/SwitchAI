package com.wstxda.switchai.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wstxda.switchai.databinding.ListItemAssistantCategoryBinding
import com.wstxda.switchai.databinding.ListItemAssistantViewBinding
import com.wstxda.switchai.databinding.ListItemReorderTipBinding
import com.wstxda.switchai.ui.viewholder.AssistantSelectorCategoryViewHolder
import com.wstxda.switchai.ui.viewholder.AssistantSelectorItemViewHolder
import com.wstxda.switchai.ui.viewholder.ReorderTipViewHolder
import com.wstxda.switchai.utils.Constants
import java.util.Collections

class AssistantSelectorAdapter(
    private val onAssistantClicked: (String) -> Unit,
    private val onPinClicked: (String) -> Unit,
    private val onDismissTipClicked: () -> Unit,
) : ListAdapter<AssistantSelectorRecyclerView, RecyclerView.ViewHolder>(
    AssistantSelectorDiffCallback()
) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is AssistantSelectorRecyclerView.CategoryHeader -> Constants.VIEW_TYPE_CATEGORY_HEADER
        is AssistantSelectorRecyclerView.AssistantSelector -> Constants.VIEW_TYPE_ASSISTANT_ITEM
        is AssistantSelectorRecyclerView.ReorderTip -> Constants.VIEW_TYPE_REORDER_TIP
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

        Constants.VIEW_TYPE_REORDER_TIP -> {
            val binding = ListItemReorderTipBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            ReorderTipViewHolder(binding)
        }

        else -> throw IllegalArgumentException("Unknown viewType $viewType")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        when (val item = getItem(pos)) {
            is AssistantSelectorRecyclerView.CategoryHeader -> (holder as AssistantSelectorCategoryViewHolder).bind(
                item
            )

            is AssistantSelectorRecyclerView.AssistantSelector -> (holder as AssistantSelectorItemViewHolder).bind(
                item, onAssistantClicked, onPinClicked
            )

            is AssistantSelectorRecyclerView.ReorderTip -> (holder as ReorderTipViewHolder).bind(
                onDismissTipClicked
            )
        }
    }

    fun moveItem(fromPosition: Int, toPosition: Int) {
        submitList(currentList.toMutableList().apply {
            Collections.swap(this, fromPosition, toPosition)
        })
    }
}