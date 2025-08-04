package com.wstxda.switchai.ui.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.wstxda.switchai.R
import com.wstxda.switchai.databinding.ListItemAssistantViewBinding
import com.wstxda.switchai.ui.adapter.AssistantSelectorRecyclerView
import com.wstxda.switchai.ui.utils.VibrationService.buttonVibration

class AssistantSelectorItemViewHolder(
    private val binding: ListItemAssistantViewBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        wrapper: AssistantSelectorRecyclerView.AssistantSelector,
        onAssistantClicked: (String) -> Unit,
        onPinClicked: (String) -> Unit,
    ) {
        val item = wrapper.assistantItem
        binding.assistantCheckedTextView.text = item.name

        val isItemEnabled = item.isInstalled
        binding.pinButton.visibility = if (isItemEnabled) View.VISIBLE else View.GONE

        binding.assistantIcon.setImageResource(
            if (item.iconRes != 0) item.iconRes else R.drawable.ic_assistant
        )

        binding.pinButton.setIconResource(
            if (item.isPinned) R.drawable.ic_pin_filled else R.drawable.ic_pin_outline
        )

        binding.pinButton.setOnClickListener {
            onPinClicked(item.key)
            it.context.buttonVibration()
        }

        itemView.setOnClickListener {
            onAssistantClicked(item.key)
        }
    }
}