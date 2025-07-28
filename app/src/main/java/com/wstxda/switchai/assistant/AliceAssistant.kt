package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.launchAssistant

class AliceAssistant : AssistantActivity() {
    override fun onCreateInternal() {
        launchAssistant(
            intents = listOf(createAliceIntent()),
            errorMessageResId = R.string.assistant_application_not_found,
            packageName = "com.yandex.aliceapp"
        )
    }

    private fun createAliceIntent() = Intent().apply {
        component = ComponentName(
            "com.yandex.aliceapp", "com.yandex.aliceapp.ui.MainActivity"
        )
    }
}