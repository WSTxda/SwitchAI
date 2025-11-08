package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class ZapiaAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.brainlogic.zapia"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createZapiaIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createZapiaIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.brainlogic.zapia.MainActivity",
        voiceInputActivity = "com.brainlogic.zapia.MainActivity"
    )
}