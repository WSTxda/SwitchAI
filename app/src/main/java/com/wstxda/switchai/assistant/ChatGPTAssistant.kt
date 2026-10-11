package com.wstxda.switchai.assistant

import androidx.lifecycle.lifecycleScope
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.logic.openAssistantRoot
import com.wstxda.switchai.utils.AssistantProperties
import com.wstxda.switchai.utils.Constants
import kotlinx.coroutines.launch

class ChatGPTAssistant : AssistantActivity() {
    private val preferences by lazy { PreferenceHelper(this) }

    companion object : AssistantProperties {
        override val packageName = "com.openai.chatgpt"
    }

    override fun onCreateInternal() {
        lifecycleScope.launch {
            if (preferences.getBoolean(Constants.VOICE_INPUT_ROOT_PREF_KEY)) {
                openChatGPTRoot()
            } else {
                openChatGPT()
            }
        }
    }

    private fun openChatGPTRoot() {
        openAssistantRoot(
            intents = listOf(createChatGPTRootIntent()),
            rootAccessMessage = R.string.root_access_warning,
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

    private fun createChatGPTRootIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.openai.chatgpt.MainActivity",
        voiceInputActivity = "com.openai.voice.assistant.AssistantActivity"
    )
}