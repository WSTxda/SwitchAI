package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class PoeAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.poe.android"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createPoeIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createPoeIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.poe.MainActivity",
        voiceInputActivity = "com.poe.MainActivity"
    )
}