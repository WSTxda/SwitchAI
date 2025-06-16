package com.wstxda.switchai.ui.viewholder

import android.content.Intent
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.wstxda.switchai.R
import com.wstxda.switchai.utils.DigitalAssistantMap
import com.wstxda.switchai.databinding.ListItemAssistantViewBinding
import com.wstxda.switchai.logic.launchAssistant
import com.wstxda.switchai.ui.adapter.AssistantSelectorRecyclerView

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
            if (item.iconResId != 0) item.iconResId else R.drawable.ic_assistant
        )
        binding.pinButton.setIconResource(
            if (item.isPinned) R.drawable.ic_pin_filled else R.drawable.ic_pin_outline
        )

        binding.pinButton.setOnClickListener { onPinClicked(item.key) }
        itemView.setOnClickListener {
            val context = it.context
            DigitalAssistantMap.assistantsMap[item.key]?.let { cls ->
                Intent(context, cls).also { intent ->
                    if (context.launchAssistant(intent)) {
                        onAssistantLaunched(item.key)
                    } else {
                        Toast.makeText(context, R.string.assistant_open_error, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } ?: Toast.makeText(context, R.string.assistant_open_error, Toast.LENGTH_SHORT).show()
        }
    }
}