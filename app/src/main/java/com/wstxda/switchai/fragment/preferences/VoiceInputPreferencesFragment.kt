package com.wstxda.switchai.fragment.preferences

import android.content.Intent
import androidx.preference.Preference
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.ShizukuSetupActivity
import com.wstxda.switchai.fragment.BasePreferenceFragment
import com.wstxda.switchai.utils.Constants

class VoiceInputPreferencesFragment : BasePreferenceFragment() {

    override val preferencesResId: Int get() = R.xml.preferences_voice_input

    override fun setupListeners() {
        setupShizukuPreference()
    }

    private fun setupShizukuPreference() {
        findPreference<Preference>(Constants.VOICE_INPUT_SHIZUKU_SETUP_PREF_KEY)?.setOnPreferenceClickListener {
            startActivity(
                Intent(
                    requireContext(), ShizukuSetupActivity::class.java
                ).putExtra(Constants.EXTRA_SHIZUKU_SETUP_ONLY, true)
            )
            true
        }
    }
}