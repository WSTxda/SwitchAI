package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class AccioAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.accio.android.app"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createAccioIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createAccioIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.accio.android.app.activity.MainActivity",
        voiceInputActivity = "com.accio.android.app.activity.MainActivity"
    )
}