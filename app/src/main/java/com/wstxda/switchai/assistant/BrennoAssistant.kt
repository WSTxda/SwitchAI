package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class BrennoAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.heytap.speechassist"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createBrennoIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createBrennoIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.heytap.speechassist.launcher.SpeechAssistMainActivity",
        voiceInputActivity = "com.heytap.speechassist.business.lockscreen.FloatSpeechActivity"
    )
}