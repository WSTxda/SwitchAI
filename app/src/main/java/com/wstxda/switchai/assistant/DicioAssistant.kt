package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class DicioAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "org.stypox.dicio"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createDicioIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createDicioIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "org.stypox.dicio.MainActivity",
        voiceInputActivity = "org.stypox.dicio.io.input.stt_popup.SttPopupActivity"
    )
}