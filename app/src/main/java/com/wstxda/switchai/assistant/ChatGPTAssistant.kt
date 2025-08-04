package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class ChatGPTAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.openai.chatgpt"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createChatGPTIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createChatGPTIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "com.openai.voice.assistant.AssistantActivity"
        )
    }
}