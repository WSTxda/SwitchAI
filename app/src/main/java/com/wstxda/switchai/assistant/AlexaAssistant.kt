package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.utils.AssistantProperties
import com.wstxda.switchai.logic.launchAssistant

class AlexaAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.amazon.dee.app"
    }

    override fun onCreateInternal() {
        launchAssistant(
            intents = listOf(createAlexaIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createAlexaIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "com.amazon.alexa.voice.ui.VoiceActivity"
        )
    }
}