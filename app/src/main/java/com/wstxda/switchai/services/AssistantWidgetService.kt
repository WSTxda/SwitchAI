package com.wstxda.switchai.services

import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.utils.DigitalAssistantMap

class AssistantWidgetService : AssistantActivity() {
    override fun onCreateInternal() {
        DigitalAssistantMap.launchSelectedAssistant(this)
        finish()
    }
}