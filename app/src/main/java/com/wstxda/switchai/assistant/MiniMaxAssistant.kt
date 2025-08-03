package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.launchAssistant
import com.wstxda.switchai.utils.AssistantProperties

class MiniMaxAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.minimax.ai"
    }

    override fun onCreateInternal() {
        launchAssistant(
            intents = listOf(createMiniMaxIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createMiniMaxIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "com.xproducer.yingshi.app.MainSplashActivity"
        )
    }
}