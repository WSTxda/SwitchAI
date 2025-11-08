package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class KrutiAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.app.krutrim.prod"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createKrutiIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createKrutiIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.app.krutrim.presentation.ui.screens.SiliconActivity",
        voiceInputActivity = "com.app.krutrim.presentation.ui.screens.SiliconActivity"
    )
}