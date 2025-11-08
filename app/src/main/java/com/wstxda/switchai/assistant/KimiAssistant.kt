package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class KimiAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.moonshot.kimichat"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createKimiIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createKimiIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.moonshot.kimichat.MainActivity",
        voiceInputActivity = "com.moonshot.kimichat.MainActivity"
    )
}