package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.launchAssistant

class ZapiaAssistant : AssistantActivity() {
    override fun onCreateInternal() {
        launchAssistant(
            intents = listOf(createZapiaIntent()),
            errorMessageResId = R.string.assistant_application_not_found,
            packageName = "com.brainlogic.zapia"
        )
    }

    private fun createZapiaIntent() = Intent().apply {
        component = ComponentName(
            "com.brainlogic.zapia", "com.brainlogic.zapia.MainActivity"
        )
    }
}