package com.wstxda.switchai.assistant

import androidx.lifecycle.lifecycleScope
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.logic.openAssistantRoot
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
                openGeminiRoot()
            } else {
                openGemini()
            }
        }
    }

    private fun openGeminiRoot() {
        openAssistantRoot(
            intents = listOf(createGeminiRootIntent()),
            rootAccessMessage = R.string.root_access_warning,
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun openGemini() {
        openAssistant(
            intents = listOf(createGeminiIntent()),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun createGeminiIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.google.android.apps.bard.shellapp.BardEntryPointActivity",
        voiceInputActivity = "com.google.android.apps.bard.shellapp.BardEntryPointActivity"
    )

    private fun createGeminiRootIntent() = createAssistantIntent(
        packageName = "com.google.android.googlequicksearchbox",
        defaultActivity = "com.google.android.apps.search.assistant.surfaces.voice.robin.main.MainActivity",
        voiceInputActivity = "com.google.android.apps.search.assistant.surfaces.voice.robin.ui.floaty.activity.FloatyActivity"
    )
}