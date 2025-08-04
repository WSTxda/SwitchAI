package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class UltimateAlexaAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.customsolutions.android.alexa"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createUltimateAlexaIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createUltimateAlexaIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "com.customsolutions.android.alexa.MainActivity"
        )
    }
}