package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.utils.AssistantProperties
import com.wstxda.switchai.logic.launchAssistant

class LeChatAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "ai.mistral.chat"
    }

    override fun onCreateInternal() {
        launchAssistant(
            intents = listOf(createLeChatIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createLeChatIntent() = Intent().apply {
        component = ComponentName(
            packageName, "ai.mistral.chat.MainActivity"
        )
    }
}