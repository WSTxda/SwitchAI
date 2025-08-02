package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.utils.AssistantProperties
import com.wstxda.switchai.logic.launchAssistant

class AliceAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.yandex.aliceapp"
    }

    override fun onCreateInternal() {
        launchAssistant(
            intents = listOf(createAliceIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createAliceIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "com.yandex.aliceapp.ui.MainActivity"
        )
    }
}