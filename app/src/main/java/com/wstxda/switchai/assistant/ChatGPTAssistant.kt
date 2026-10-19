package com.wstxda.switchai.assistant

import android.content.Intent
import androidx.core.net.toUri
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.logic.openAssistantShizuku
import com.wstxda.switchai.utils.AssistantProperties
import com.wstxda.switchai.utils.Constants

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
        val intents = buildList {
            if (preferences.getBoolean(Constants.VOICE_INPUT_PREF_KEY, true)) {
                add(Intent(Intent.ACTION_VIEW, "https://chatgpt.com/voice".toUri())
                    .setPackage(Companion.packageName))
            }
            add(createChatGPTIntent())
        }
        openAssistant(
            intents = intents,
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
