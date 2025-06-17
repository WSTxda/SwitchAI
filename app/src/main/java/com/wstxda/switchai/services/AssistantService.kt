package com.wstxda.switchai.services

import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.utils.DigitalAssistantMap

class AssistantService : AssistantActivity() {
    override fun onCreateInternal() {
        DigitalAssistantMap.launchSelectedAssistant(this)
        finish()
    }
}