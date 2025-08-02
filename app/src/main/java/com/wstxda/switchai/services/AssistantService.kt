package com.wstxda.switchai.services

import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.utils.Assistant

class AssistantService : AssistantActivity() {
    override fun onCreateInternal() {
        Assistant.open(this)
        finish()
    }
}