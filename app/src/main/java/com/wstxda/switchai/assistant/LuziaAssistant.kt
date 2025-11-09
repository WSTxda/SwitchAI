package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class LuziaAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "co.thewordlab.luzia"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createLuziaIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createLuziaIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "co.thewordlab.luzia.ui.activity.launch.LaunchActivity",
        voiceInputActivity = "co.thewordlab.luzia.ui.activity.launch.LaunchActivity"
    )
}