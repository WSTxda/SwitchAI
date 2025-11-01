package com.wstxda.switchai.assistant

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

    private fun createHomeIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "io.homeassistant.companion.android.launch.LaunchActivity",
        voiceInputActivity = "io.homeassistant.companion.android.assist.AssistActivity"
    )
}