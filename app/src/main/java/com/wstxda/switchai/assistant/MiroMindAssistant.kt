package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class MiroMindAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "ai.miromind.app"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createMiroMindIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createMiroMindIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.tanka.npc.ui.activity.LaunchActivity",
        voiceInputActivity = "com.tanka.npc.ui.activity.LaunchActivity"
    )
}