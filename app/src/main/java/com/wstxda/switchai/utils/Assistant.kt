package com.wstxda.switchai.utils

import android.content.Context
import android.content.Intent
import com.wstxda.switchai.logic.PreferenceHelper

object Assistant {

    fun open(context: Context) {
        val preferenceHelper = PreferenceHelper(context)
        val selectedShortcut = preferenceHelper.getString(
            Constants.DIGITAL_ASSISTANT_SELECT_PREF_KEY, null
        ) ?: return

        val activityClass = AssistantsMap.assistants[selectedShortcut] ?: return
        val intent = Intent(context, activityClass).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}