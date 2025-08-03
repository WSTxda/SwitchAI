package com.wstxda.switchai.assistant

import android.content.ComponentName
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.logic.launchAssistant
import com.wstxda.switchai.logic.launchAssistantRoot
import com.wstxda.switchai.utils.AssistantProperties
import com.wstxda.switchai.utils.Constants
import kotlinx.coroutines.launch

class GeminiAssistant : AssistantActivity() {
    private val preferences by lazy { PreferenceHelper(this) }

    companion object : AssistantProperties {
        override val packageName = "com.google.android.apps.bard"
    }

    override fun onCreateInternal() {
        lifecycleScope.launch {
            if (preferences.getBoolean(Constants.ASSISTANT_ROOT_PREF_KEY)) {
                launchGeminiFloaty()
            } else {
                launchGemini()
            }
        }
    }

    private fun launchGeminiFloaty() {
        launchAssistantRoot(
            intents = listOf(createGeminiFloatyIntent()),
            rootAccessMessage = R.string.root_access_warning,
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun launchGemini() {
        launchAssistant(
            intents = listOf(createGeminiIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createGeminiIntent() = Intent().apply {
        component = ComponentName(
            Companion.packageName, "com.google.android.apps.bard.shellapp.BardEntryPointActivity"
        )
    }

    private fun createGeminiFloatyIntent() = Intent().apply {
        component = ComponentName(
            "com.google.android.googlequicksearchbox",
            "com.google.android.apps.search.assistant.surfaces.voice.robin.ui.floaty.activity.FloatyActivity"
        )
    }
}