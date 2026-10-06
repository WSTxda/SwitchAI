package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.logic.openAssistantShizuku
import com.wstxda.switchai.utils.AssistantProperties

class GeminiAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.google.android.apps.bard"
    }

    override fun onCreateInternal() {
        if (launchSettings.shizukuVoiceInput) openGeminiShizuku() else openGemini()
    }

    private fun openGeminiShizuku() {
        openAssistantShizuku(
            intents = listOf(createGeminiShizukuIntent()),
            errorMessage = R.string.assistant_application_not_found,
        )
    }

    private fun openGemini() {
        openAssistant(
            packageName = Companion.packageName,
            intents = listOf(createGeminiIntent()),
            errorMessage = R.string.assistant_application_not_found,
        )
    }

    private fun createGeminiIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.google.android.apps.bard.shellapp.BardEntryPointActivity",
        voiceInputActivity = "com.google.android.apps.bard.shellapp.BardEntryPointActivity",
    )

    private fun createGeminiShizukuIntent() = createAssistantIntent(
        packageName = "com.google.android.googlequicksearchbox",
        defaultActivity = "com.google.android.apps.search.assistant.surfaces.voice.robin.main.MainActivity",
        voiceInputActivity = "com.google.android.apps.search.assistant.surfaces.voice.robin.ui.floaty.activity.FloatyActivity",
    )
}