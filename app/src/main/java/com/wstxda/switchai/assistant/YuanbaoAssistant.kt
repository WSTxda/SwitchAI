package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.logic.openAssistantShizuku
import com.wstxda.switchai.utils.AssistantProperties

class YuanbaoAssistant : AssistantActivity() {
    private val preferences by lazy { PreferenceHelper(this) }

    companion object : AssistantProperties {
        override val packageName = "com.tencent.hunyuan.app.chat"
    }

    override fun onCreateInternal() {
        if (preferences.isShizukuVoiceInputEnabled()) {
            openYuanbaoShizuku()
        } else {
            openYuanbao()
        }
    }

    private fun openYuanbaoShizuku() {
        openAssistantShizuku(
            intent = createYuanbaoShizukuIntent(),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun openYuanbao() {
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

    private fun createYuanbaoShizukuIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.tencent.hunyuan.app.chat.biz.login.v2.HYLoginMainActivity",
        voiceInputActivity = "com.tencent.hunyuan.app.chat.biz.chats.conversation.hyvoicecall.HYVoiceCallActivity"
    )
}