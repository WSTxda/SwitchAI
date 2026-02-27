package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class  QwenAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "ai.qwenlm.chat.android"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createQwenIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createQwenIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "ai.qwenlm.chat.android.MainActivity",
        voiceInputActivity = "ai.qwenlm.chat.android.MainActivity"
    )
}