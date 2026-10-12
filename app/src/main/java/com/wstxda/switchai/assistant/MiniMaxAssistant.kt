package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class MiniMaxAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.minimax.ai"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createMiniMaxIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createMiniMaxIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.xproducer.yingshi.app.MainSplashActivity",
        voiceInputActivity = "com.xproducer.yingshi.app.MainSplashActivity"
    )
}