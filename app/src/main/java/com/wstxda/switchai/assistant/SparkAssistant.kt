package com.wstxda.switchai.assistant

import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.utils.AssistantProperties

class SparkAssistant : AssistantActivity() {

    companion object : AssistantProperties {
        override val packageName = "com.iflytek.spark"
    }

    override fun onCreateInternal() {
        openAssistant(
            intents = listOf(createSparkIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createSparkIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.iflytek.spark.MainActivity",
        voiceInputActivity = "com.iflytek.spark.MainActivity"
    )
}