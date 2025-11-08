package com.wstxda.switchai.assistant

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

    private fun createGrokIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "ai.x.grok.main.GrokActivity",
        voiceInputActivity = "ai.x.grok.main.GrokActivity"
    )
}