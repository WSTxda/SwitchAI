package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class BixbyAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.samsung.android.bixby.agent"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createBixbyIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createBixbyIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.samsung.android.bixby.assistanthome.deeplink.DeepLinkPublicActivity",
        voiceInputActivity = "com.samsung.android.bixby.assistanthome.AssistantHomeLauncherActivity"
    )
}