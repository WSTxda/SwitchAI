package com.wstxda.switchai.activity

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.utils.Constants

abstract class AssistantActivity : BaseActivity() {

    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateInternal()
        finish()
    }

    abstract fun onCreateInternal()

    fun createAssistantIntent(
        packageName: String, voiceInputActivity: String, defaultActivity: String
    ): Intent {
        preferenceHelper = PreferenceHelper(this)
        val useVoiceInput = preferenceHelper.getBoolean(Constants.ASSISTANT_VOICE_PREF_KEY, true)

        val className = if (useVoiceInput) voiceInputActivity else defaultActivity

        return Intent().apply {
            component = ComponentName(packageName, className)
        }
    }
}