package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class MotoAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.motorola.uxcore"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createMotoIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createMotoIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.motorola.foryou.MainActivity",
        voiceInputActivity = "com.motorola.eldin.DoublePressMotoAI"
    )
}