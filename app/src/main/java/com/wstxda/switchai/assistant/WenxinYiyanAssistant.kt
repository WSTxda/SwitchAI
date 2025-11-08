package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class WenxinYiyanAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.baidu.newapp"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createWenxinYiyanIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createWenxinYiyanIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.baidu.newapp.home.MainActivity",
        voiceInputActivity = "com.baidu.newapp.home.MainActivity"
    )
}