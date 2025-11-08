package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class LumoAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "me.proton.android.lumo"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createLumoIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createLumoIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "me.proton.android.lumo.MainActivity",
        voiceInputActivity = "me.proton.android.lumo.MainActivity"
    )
}