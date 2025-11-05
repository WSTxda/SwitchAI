package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class KrutiAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.app.krutrim.prod"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createDeepSeekIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createDeepSeekIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "com.app.krutrim.presentation.ui.screens.SiliconActivity"
        )
    }
}