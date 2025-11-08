package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class QingyanAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.zhipuai.qingyan"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createQingyanIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createQingyanIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.zhipuai.qingyan.MainActivity",
        voiceInputActivity = "com.zhipuai.qingyan.MainActivity"
    )
}