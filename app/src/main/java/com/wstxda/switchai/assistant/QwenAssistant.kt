package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.launchAssistant
import com.wstxda.switchai.utils.AssistantProperties

class QwenAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "ai.qwenlm.chat.android"
    }

    override fun onCreateInternal() {
        launchAssistant(
            intents = listOf(createQwenIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createQwenIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "ai.qwenlm.chat.android.MainActivity"
        )
    }
}