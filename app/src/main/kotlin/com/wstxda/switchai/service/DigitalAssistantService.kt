package com.wstxda.switchai.service

import android.content.Intent
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.activity.AssistantSelectorActivity
import com.wstxda.switchai.utils.Assistant

class DigitalAssistantService : AssistantActivity() {

    override fun onCreateInternal() {
        if (launchSettings.selectorEnabled) startActivity(
            Intent(
                this, AssistantSelectorActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        else Assistant.open(this)
    }
}