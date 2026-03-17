package com.wstxda.switchai.fragment.preferences

import android.os.Bundle
import com.wstxda.switchai.R

class VoiceInputPreferencesFragment : BasePreferencesFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_voice_input, rootKey)
    }
}