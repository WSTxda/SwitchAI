package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.utils.AssistantProperties
import com.wstxda.switchai.logic.launchAssistant

class CopilotAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.microsoft.copilot"
    }

    override fun onCreateInternal() {
        launchAssistant(
            intents = listOf(createCopilotIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createCopilotIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName,
            "com.microsoft.copilotn.features.digitalassistant.AssistantOverlayActivity"
        )
    }
}