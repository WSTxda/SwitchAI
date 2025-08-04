package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class GrokAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "ai.x.grok"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createGrokIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createGrokIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "ai.x.grok.main.GrokActivity"
        )
    }
}