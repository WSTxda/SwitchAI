package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.logic.openAssistantShizuku
import com.wstxda.switchai.utils.AssistantProperties

class MarusyaAssistant : AssistantActivity() {
    private val preferences by lazy { PreferenceHelper(this) }

    companion object : AssistantProperties {
        override val packageName = "ru.mail.search.electroscope"
    }

    override fun onCreateInternal() {
        if (preferences.isShizukuVoiceInputEnabled()) {
            openMarusyaShizuku()
        } else {
            openMarusya()
        }
    }

    private fun openMarusyaShizuku() {
        openAssistantShizuku(
            intent = createMarusyaShizukuIntent(),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun openMarusya() {
        openAssistant(
            intents = listOf(createMarusyaIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createMarusyaIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "ru.mail.search.electroscope.ui.InputTextActivity",
        voiceInputActivity = "ru.mail.search.electroscope.ui.activity.AssistantActivity"
    )

    private fun createMarusyaShizukuIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "ru.mail.search.electroscope.ui.InputTextActivity",
        voiceInputActivity = "ru.mail.search.electroscope.defaultassistant.presentation.keyguard.DefaultAssistantSessionActivity"
    )
}