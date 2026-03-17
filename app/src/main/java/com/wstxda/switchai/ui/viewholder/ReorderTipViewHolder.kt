package com.wstxda.switchai.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.wstxda.switchai.databinding.ListItemAssistantReorderTipBinding

class ReorderTipViewHolder(
    private val binding: ListItemAssistantReorderTipBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(onDismissClicked: () -> Unit) {
        binding.dismissButton.setOnClickListener {
            onDismissClicked()
        }
    }
}