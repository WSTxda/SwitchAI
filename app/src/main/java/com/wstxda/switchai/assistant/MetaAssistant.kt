package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class MetaAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.facebook.stella"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createMetaIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createMetaIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.facebook.stella.main.view.MainActivity",
        voiceInputActivity = "com.facebook.stella.main.view.MainActivity"
    )
}