package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class StepFunAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "cn.yuewen.ywapp"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createStepFunIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createStepFunIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "cn.yuewen.ywapp.MainActivity",
        voiceInputActivity = "cn.yuewen.ywapp.MainActivity"
    )
}