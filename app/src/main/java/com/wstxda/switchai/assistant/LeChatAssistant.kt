package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class LeChatAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "ai.mistral.chat"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createLeChatIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createLeChatIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "ai.mistral.chat.MainActivity",
        voiceInputActivity = "ai.mistral.chat.MainActivity"
    )
}