package com.wstxda.switchai.assistant

import androidx.lifecycle.lifecycleScope
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.logic.openAssistantRoot
import com.wstxda.switchai.utils.AssistantProperties
import com.wstxda.switchai.utils.Constants
import kotlinx.coroutines.launch

class YuanbaoAssistant : AssistantActivity() {
    private val preferences by lazy { PreferenceHelper(this) }

    companion object : AssistantProperties {
        override val packageName = "com.tencent.hunyuan.app.chat"
    }

    override fun onCreateInternal() {
        lifecycleScope.launch {
            if (preferences.getBoolean(Constants.ASSISTANT_ROOT_PREF_KEY)) {
                openYuanbaoRoot()
            } else {
                openYuanbao()
            }
        }
    }

    private fun openYuanbaoRoot() {
        openAssistantRoot(
            intents = listOf(createYuanbaoRootIntent()),
            rootAccessMessage = R.string.root_access_warning,
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

    private fun createYuanbaoRootIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.tencent.hunyuan.app.chat.biz.login.v2.HYLoginMainActivity",
        voiceInputActivity = "com.tencent.hunyuan.app.chat.biz.chats.conversation.hyvoicecall.HYVoiceCallActivity"
    )
}