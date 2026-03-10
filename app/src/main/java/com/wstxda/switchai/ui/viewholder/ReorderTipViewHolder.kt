package com.wstxda.switchai.ui.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.wstxda.switchai.databinding.ListItemReorderTipBinding

class ReorderTipViewHolder(
    private val binding: ListItemReorderTipBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(onDismissClicked: () -> Unit, isGridMode: Boolean = false) {
        binding.dismissButton.setOnClickListener {
            onDismissClicked()
        }
        if (isGridMode) {
            val lp = binding.root.layoutParams as? android.view.ViewGroup.MarginLayoutParams
            lp?.marginStart = 0
            lp?.marginEnd = 0
            binding.root.layoutParams = lp
        }
    }
}