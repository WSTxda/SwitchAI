package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class XiaoAiAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.miui.voiceassist"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createXiaoAiIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createXiaoAiIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.xiaomi.voiceassistant.LaunchHomeRouterActivity",
        voiceInputActivity = "com.xiaomi.voiceassistant.MiuiVoiceAssistActivity"
    )
}