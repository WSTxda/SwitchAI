package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class ManusAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "tech.butterfly.app"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createManusIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createManusIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "tech.butterfly.app.MainActivity",
        voiceInputActivity = "tech.butterfly.app.MainActivity"
    )
}