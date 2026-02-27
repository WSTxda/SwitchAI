package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class MonicaAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "im.monica.app.monica"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createMonicaIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createMonicaIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "im.monica.app.monica.MainActivity",
        voiceInputActivity = "im.monica.app.monica.MainActivity"
    )
}