package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class DelphiAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "ai.oo.delphi"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createDelphiIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createDelphiIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "ai.oo.delphi.MainActivity",
        voiceInputActivity = "ai.oo.delphi.MainActivity"
    )
}