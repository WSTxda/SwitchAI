package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class TianxiAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.lenovo.menu_assistant"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createTianxiIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createTianxiIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.lenovo.menu_assistant.MainActivity",
        voiceInputActivity = "com.lenovo.menu_assistant.MainActivity"
    )
}