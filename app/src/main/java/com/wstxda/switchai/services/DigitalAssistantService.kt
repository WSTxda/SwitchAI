package com.wstxda.switchai.services

import android.content.Intent
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.activity.AssistantSelectorActivity
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.utils.Assistant
import com.wstxda.switchai.utils.Constants

class DigitalAssistantService : AssistantActivity() {
    override fun onCreateInternal() {
        val showSelector =
            PreferenceHelper(this).getBoolean(Constants.ASSISTANT_SELECTOR_DIALOG_PREF_KEY, true)

        if (showSelector) {
            startActivity(Intent(this, AssistantSelectorActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
        } else {
            Assistant.open(this)
        }
        finish()
    }
}