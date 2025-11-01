package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class PerplexityAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "ai.perplexity.app.android"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createPerplexityIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createPerplexityIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "ai.perplexity.app.android.ui.main.MainActivity",
        voiceInputActivity = "ai.perplexity.app.android.assistant.AssistantActivity"
    )
}