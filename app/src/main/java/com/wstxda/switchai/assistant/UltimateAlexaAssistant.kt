package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class UltimateAlexaAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.customsolutions.android.alexa"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createUltimateAlexaIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createUltimateAlexaIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.customsolutions.android.alexa.MainActivity",
        voiceInputActivity = "com.customsolutions.android.alexa.MainActivity"
    )
}