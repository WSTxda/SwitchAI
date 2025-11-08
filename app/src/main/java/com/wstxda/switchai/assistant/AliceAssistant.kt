package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class AliceAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.yandex.aliceapp"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createAliceIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createAliceIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.yandex.aliceapp.ui.MainActivity",
        voiceInputActivity = "com.yandex.aliceapp.ui.MainActivity"
    )
}