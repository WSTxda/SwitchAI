package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class OxproxionAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "io.github.stardomains3.oxproxion"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createKimiIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createKimiIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "io.github.stardomains3.oxproxion.MainActivity",
        voiceInputActivity = "io.github.stardomains3.oxproxion.MainActivity"
    )
}