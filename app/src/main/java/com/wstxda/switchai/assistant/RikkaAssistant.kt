package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class RikkaAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "me.rerere.rikkahub"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createRikkaIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createRikkaIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "me.rerere.rikkahub.RouteActivity",
        voiceInputActivity = "me.rerere.rikkahub.RouteActivity"
    )
}