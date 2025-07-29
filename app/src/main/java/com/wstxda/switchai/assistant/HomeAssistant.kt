package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.launchAssistant

class HomeAssistant : AssistantActivity() {
    override fun onCreateInternal() {
        launchAssistant(
            intents = listOf(createHomeIntent()),
            errorMessageResId = R.string.assistant_application_not_found,
            packageName = "io.homeassistant.companion.android"
        )
    }

    private fun createHomeIntent() = Intent().apply {
        component = ComponentName(
            "io.homeassistant.companion.android",
            "io.homeassistant.companion.android.assist.AssistActivity"
        )
    }
}