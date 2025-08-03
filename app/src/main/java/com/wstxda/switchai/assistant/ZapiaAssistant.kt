package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.launchAssistant
import com.wstxda.switchai.utils.AssistantProperties

class ZapiaAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.brainlogic.zapia"
    }

    override fun onCreateInternal() {
        launchAssistant(
            intents = listOf(createZapiaIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createZapiaIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "com.brainlogic.zapia.MainActivity"
        )
    }
}