package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class DeepSeekAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.deepseek.chat"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createDeepSeekIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createDeepSeekIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.deepseek.chat.MainActivity",
        voiceInputActivity = "com.deepseek.chat.MainActivity"
    )
}