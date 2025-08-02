package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.utils.AssistantProperties
import com.wstxda.switchai.logic.launchAssistant

class ClaudeAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.anthropic.claude"
    }

    override fun onCreateInternal() {
        launchAssistant(
            intents = listOf(createClaudeIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createClaudeIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "com.anthropic.claude.mainactivity.MainActivity"
        )
    }
}