package com.wstxda.switchai.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.wstxda.switchai.data.TutorialItem
import com.wstxda.switchai.databinding.ListItemAssistantTutorialBinding

object TutorialItemViewBuilder {
    fun build(context: Context, item: TutorialItem): View {
        val binding = ListItemAssistantTutorialBinding.inflate(LayoutInflater.from(context))
        binding.tutorialImage.setImageResource(item.imageRes)
        binding.tutorialTitle.setText(item.titleRes)
        binding.tutorialTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(item.iconRes, 0, 0, 0)
        binding.tutorialSummary.setText(item.summaryRes)
        return binding.root
    }
}