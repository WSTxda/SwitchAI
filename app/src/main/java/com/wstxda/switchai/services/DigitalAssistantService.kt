package com.wstxda.switchai.services

import android.content.Intent
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.activity.AssistantSelectorActivity
import com.wstxda.switchai.utils.DigitalAssistantMap
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.utils.Constants

class DigitalAssistantService : AssistantActivity() {
    override fun onCreateInternal() {
        val preferenceHelper = PreferenceHelper(this)
        val showSelector =
            preferenceHelper.getBoolean(Constants.ASSISTANT_SELECTOR_DIALOG_PREF_KEY, true)

        if (showSelector) {
            val intent = Intent(this, AssistantSelectorActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        } else {
            DigitalAssistantMap.launchSelectedAssistant(this)
        }
        finish()
    }
}