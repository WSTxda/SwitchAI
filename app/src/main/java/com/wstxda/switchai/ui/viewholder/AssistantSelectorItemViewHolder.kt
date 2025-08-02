package com.wstxda.switchai.ui.viewholder

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.wstxda.switchai.R
import com.wstxda.switchai.utils.AssistantsMap
import com.wstxda.switchai.databinding.ListItemAssistantViewBinding
import com.wstxda.switchai.ui.adapter.AssistantSelectorRecyclerView
import com.wstxda.switchai.ui.utils.VibrationService.buttonVibration

class AssistantSelectorItemViewHolder(
    private val binding: ListItemAssistantViewBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        wrapper: AssistantSelectorRecyclerView.AssistantSelector,
        onAssistantLaunched: (String) -> Unit,
        onPinClicked: (String) -> Unit,
    ) {
        val item = wrapper.assistantItem
        binding.assistantCheckedTextView.text = item.name
        binding.assistantIcon.setImageResource(
            if (item.iconRes != 0) item.iconRes else R.drawable.ic_assistant
        )
        binding.pinButton.setIconResource(
            if (item.isPinned) R.drawable.ic_pin_filled else R.drawable.ic_pin_outline
        )

        if (item.isInstalled) {
            binding.pinButton.visibility = View.VISIBLE
            binding.pinButton.setOnClickListener {
                onPinClicked(item.key)
                it.context.buttonVibration()
            }
        } else {
            binding.pinButton.visibility = View.GONE
        }

        itemView.setOnClickListener {
            val context = it.context
            AssistantsMap.assistants[item.key]?.let { cls ->
                val intent = Intent(context, cls).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
                onAssistantLaunched(item.key)
            } ?: Toast.makeText(context, R.string.assistant_open_error, Toast.LENGTH_SHORT).show()
        }
    }
}