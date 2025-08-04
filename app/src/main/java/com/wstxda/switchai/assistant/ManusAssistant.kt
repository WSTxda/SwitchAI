package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class ManusAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "tech.butterfly.app"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createManusIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createManusIntent() = Intent().apply {
        component = ComponentName(
            packageName, "tech.butterfly.app.MainActivity"
        )
    }
}