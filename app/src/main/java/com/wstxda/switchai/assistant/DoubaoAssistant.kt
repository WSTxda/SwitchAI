package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class DoubaoAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.larus.nova"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createDoubaoIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createDoubaoIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "com.larus.home.impl.MainActivity"
        )
    }
}