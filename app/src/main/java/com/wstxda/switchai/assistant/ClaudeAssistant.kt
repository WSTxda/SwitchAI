package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class ClaudeAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.anthropic.claude"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createClaudeIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createClaudeIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.anthropic.claude.mainactivity.MainActivity",
        voiceInputActivity = "com.anthropic.claude.mainactivity.MainActivity"
    )
}