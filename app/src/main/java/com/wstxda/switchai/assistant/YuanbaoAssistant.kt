package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class YuanbaoAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.tencent.hunyuan.app.chat"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createYuanbaoIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createYuanbaoIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.tencent.hunyuan.app.chat.biz.login.v2.HYLoginMainActivity",
        voiceInputActivity = "com.tencent.hunyuan.app.chat.biz.login.v2.HYLoginMainActivity"
    )
}