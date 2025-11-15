package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class EuriaAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.infomaniak.euria"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createEuriaIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createEuriaIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.infomaniak.euria.MainActivity",
        voiceInputActivity = "com.infomaniak.euria.MainActivity"
    )
}