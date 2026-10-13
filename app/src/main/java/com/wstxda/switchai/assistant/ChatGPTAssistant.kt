package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.logic.openAssistantShizuku
import com.wstxda.switchai.utils.AssistantProperties

class ChatGPTAssistant : AssistantActivity() {
    private val preferences by lazy { PreferenceHelper(this) }

    companion object : AssistantProperties {
        override val packageName = "com.openai.chatgpt"
    }

    override fun onCreateInternal() {
        if (preferences.isShizukuVoiceInputEnabled()) {
            openChatGPTShizuku()
        } else {
            openChatGPT()
        }
    }

    private fun openChatGPTShizuku() {
        openAssistantShizuku(
            intent = createChatGPTShizukuIntent(),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun openChatGPT() {
        openAssistant(
            intents = listOf(createChatGPTIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createChatGPTIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.openai.chatgpt.MainActivity",
        voiceInputActivity = "com.openai.chatgpt.MainActivity"
    )

    private fun createChatGPTShizukuIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.openai.chatgpt.MainActivity",
        voiceInputActivity = "com.openai.voice.assistant.AssistantActivity"
    )
}