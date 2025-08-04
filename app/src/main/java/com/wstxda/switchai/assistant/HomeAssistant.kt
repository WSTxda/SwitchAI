package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class HomeAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "io.homeassistant.companion.android"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createHomeIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createHomeIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName,
            "io.homeassistant.companion.android.assist.AssistActivity"
        )
    }
}