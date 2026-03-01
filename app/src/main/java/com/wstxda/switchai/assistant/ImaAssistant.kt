package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class ImaAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.tencent.ima"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createImaIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createImaIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.tencent.ima.AskImaActivity",
        voiceInputActivity = "com.tencent.ima.AskImaActivity"
    )
}