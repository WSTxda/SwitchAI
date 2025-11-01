package com.wstxda.switchai.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.wstxda.switchai.databinding.ListItemReorderTipBinding

class ReorderTipViewHolder(
    private val binding: ListItemReorderTipBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(onDismissClicked: () -> Unit) {
        binding.dismissButton.setOnClickListener {
            onDismissClicked()
        }
    }
}