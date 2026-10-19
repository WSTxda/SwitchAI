package com.wstxda.switchai.assistant

import android.content.Intent
import androidx.core.net.toUri
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.logic.openAssistant
import com.wstxda.switchai.logic.openAssistantShizuku
import com.wstxda.switchai.utils.AssistantProperties
import com.wstxda.switchai.utils.Constants

class GeminiAssistant : AssistantActivity() {
    private val preferences by lazy { PreferenceHelper(this) }

    companion object : AssistantProperties {
        override val packageName = "com.google.android.apps.bard"
    }

    override fun onCreateInternal() {
        if (preferences.isShizukuVoiceInputEnabled()) {
            openGeminiShizuku()
        } else {
            openGemini()
        }
    }

    private fun openGeminiShizuku() {
        openAssistantShizuku(
            intent = createGeminiShizukuIntent(),
            errorMessage = R.string.assistant_application_not_found
        )
    }

    private fun openGemini() {
        val intents = buildList {
            if (preferences.getBoolean(Constants.VOICE_INPUT_PREF_KEY, true)) {
                add(Intent(Intent.ACTION_VIEW, "https://gemini.google.com/app?target=live".toUri())
                    .setPackage("com.google.android.googlequicksearchbox"))
            }
            add(createGeminiIntent())
        }
        openAssistant(
            intents = intents,
            errorMessage = R.string.assistant_application_not_found,
            storePackageName = Companion.packageName
        )
    }

    private fun createGeminiIntent() = createAssistantIntent(
        packageName = Companion.packageName,
        defaultActivity = "com.google.android.apps.bard.shellapp.BardEntryPointActivity",
        voiceInputActivity = "com.google.android.apps.bard.shellapp.BardEntryPointActivity"
    )

    private fun createGeminiShizukuIntent() = createAssistantIntent(
        packageName = "com.google.android.googlequicksearchbox",
        defaultActivity = "com.google.android.apps.search.assistant.surfaces.voice.robin.main.MainActivity",
        voiceInputActivity = "com.google.android.apps.search.assistant.surfaces.voice.robin.ui.floaty.activity.FloatyActivity"
    )
}
