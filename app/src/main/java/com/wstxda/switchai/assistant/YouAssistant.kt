package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class YouAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.you.browser"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createYouIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createYouIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.you.chat.MainActivity",
        voiceInputActivity = "com.you.chat.VoiceSearchActivity"
    )
}