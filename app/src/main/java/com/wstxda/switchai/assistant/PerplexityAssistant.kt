package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.launchAssistant
import com.wstxda.switchai.utils.AssistantProperties

class PerplexityAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "ai.perplexity.app.android"
    }

    override fun onCreateInternal() {
        launchAssistant(
            intents = listOf(createPerplexityIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createPerplexityIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "ai.perplexity.app.android.assistant.AssistantActivity"
        )
    }
}