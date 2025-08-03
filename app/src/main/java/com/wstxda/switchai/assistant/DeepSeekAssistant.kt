package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.launchAssistant
import com.wstxda.switchai.utils.AssistantProperties

class DeepSeekAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.deepseek.chat"
    }

    override fun onCreateInternal() {
        launchAssistant(
            intents = listOf(createDeepSeekIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createDeepSeekIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "com.deepseek.chat.MainActivity"
        )
    }
}