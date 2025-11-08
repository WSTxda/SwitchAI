package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class PiAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "ai.inflection.pi"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createPiIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createPiIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "ai.inflection.pi.MainActivity",
        voiceInputActivity = "ai.inflection.pi.MainActivity"
    )
}