package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class LinerAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.getliner.liner"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createLinerIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createLinerIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.getliner.liner.MainActivity",
        voiceInputActivity = "com.getliner.liner.MainActivity"
    )
}