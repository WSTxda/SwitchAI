package com.wstxda.switchai.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wstxda.switchai.databinding.ListItemAssistantCategoryBinding
import com.wstxda.switchai.databinding.ListItemAssistantGridViewBinding
import com.wstxda.switchai.databinding.ListItemAssistantListViewBinding
import com.wstxda.switchai.databinding.ListItemAssistantReorderTipBinding
import com.wstxda.switchai.ui.viewholder.AssistantSelectorCategoryViewHolder
import com.wstxda.switchai.ui.viewholder.AssistantSelectorGridItemViewHolder
import com.wstxda.switchai.ui.viewholder.AssistantSelectorItemViewHolder
import com.wstxda.switchai.ui.viewholder.ReorderTipViewHolder
import com.wstxda.switchai.utils.Constants
import java.util.Collections

class AssistantSelectorAdapter(

    private val onAssistantClicked: (String) -> Unit,
    private val onPinClicked: (String) -> Unit,
    private val onDismissTipClicked: () -> Unit,
    val isGridMode: Boolean = false,
    val showCounter: Boolean = true,
    private val columnCount: Int = 1,
) : ListAdapter<AssistantSelectorRecyclerView, RecyclerView.ViewHolder>(
    AssistantSelectorDiffCallback()
) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is AssistantSelectorRecyclerView.CategoryHeader -> Constants.VIEW_TYPE_CATEGORY_HEADER
        is AssistantSelectorRecyclerView.AssistantSelector -> if (isGridMode) Constants.VIEW_TYPE_ASSISTANT_GRID_ITEM else Constants.VIEW_TYPE_ASSISTANT_ITEM
        is AssistantSelectorRecyclerView.ReorderTip -> Constants.VIEW_TYPE_REORDER_TIP
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        Constants.VIEW_TYPE_CATEGORY_HEADER -> AssistantSelectorCategoryViewHolder(
            ListItemAssistantCategoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

        Constants.VIEW_TYPE_ASSISTANT_ITEM -> AssistantSelectorItemViewHolder(
            ListItemAssistantListViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

        Constants.VIEW_TYPE_ASSISTANT_GRID_ITEM -> AssistantSelectorGridItemViewHolder(
            ListItemAssistantGridViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

        Constants.VIEW_TYPE_REORDER_TIP -> ReorderTipViewHolder(
            ListItemAssistantReorderTipBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

        else -> throw IllegalArgumentException("Unknown viewType: $viewType")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is AssistantSelectorRecyclerView.CategoryHeader -> (holder as AssistantSelectorCategoryViewHolder).bind(
                item, showCounter
            )

            is AssistantSelectorRecyclerView.AssistantSelector -> when (holder) {
                is AssistantSelectorItemViewHolder -> holder.bind(
                    item, onAssistantClicked, onPinClicked
                )

                is AssistantSelectorGridItemViewHolder -> holder.bind(
                    item, onAssistantClicked, onPinClicked, columnCount
                )

                else -> Unit
            }

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