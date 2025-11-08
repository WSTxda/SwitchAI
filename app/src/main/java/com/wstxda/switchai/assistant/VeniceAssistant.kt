package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class VeniceAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.ai.venice"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createVeniceIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createVeniceIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.ai.venice.MainActivity",
        voiceInputActivity = "com.ai.venice.MainActivity"
    )
}