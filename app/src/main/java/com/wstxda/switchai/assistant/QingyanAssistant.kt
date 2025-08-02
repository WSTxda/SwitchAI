package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.utils.AssistantProperties
import com.wstxda.switchai.logic.launchAssistant

class QingyanAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.zhipuai.qingyan"
    }

    override fun onCreateInternal() {
        launchAssistant(
            intents = listOf(createQingyanIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createQingyanIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "com.zhipuai.qingyan.MainActivity"
        )
    }
}