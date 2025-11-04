package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class PiAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "ai.inflection.pi"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createPiIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createPiIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "ai.inflection.pi.MainActivity"
        )
    }
}