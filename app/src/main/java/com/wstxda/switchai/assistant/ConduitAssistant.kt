package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class ConduitAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "app.cogwheel.conduit"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createConduitIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createConduitIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "app.cogwheel.conduit.MainActivity",
        voiceInputActivity = "app.cogwheel.conduit.MainActivity"
    )
}