package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class CopilotAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.microsoft.copilot"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createCopilotIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createCopilotIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.microsoft.copilotn.MainActivity",
        voiceInputActivity = "com.microsoft.copilotn.features.digitalassistant.AssistantOverlayActivity"
    )
}