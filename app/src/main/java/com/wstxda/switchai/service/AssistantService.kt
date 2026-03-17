package com.wstxda.switchai.service

import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.utils.Assistant

class AssistantService : AssistantActivity() {

    override fun onCreateInternal() {
        Assistant.open(this)
        finish()
    }
}