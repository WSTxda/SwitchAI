package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class AlexaAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.amazon.dee.app"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createAlexaIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createAlexaIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.amazon.dee.app.Launcher",
        voiceInputActivity = "com.amazon.alexa.voice.ui.VoiceActivity"
    )
}