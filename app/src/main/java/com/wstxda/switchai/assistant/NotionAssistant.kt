package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class NotionAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "notion.id"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createNotionIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createNotionIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "notion.local.id.MainActivity",
        voiceInputActivity = "notion.local.id.MainActivity"
    )
}