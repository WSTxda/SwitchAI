package com.wstxda.switchai.fragment.preferences

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceFragmentCompat
import com.wstxda.switchai.logic.DigitalAssistantChecker
import com.wstxda.switchai.utils.Constants

class DigitalAssistantPreference(private val fragment: PreferenceFragmentCompat) {

    private val context: Context get() = fragment.requireContext()

    fun checkDigitalAssistSetupStatus(): Boolean = DigitalAssistantChecker.isSetupDone(context)

    fun setDigitalAssistSetupStatus(isSetupDone: Boolean) {
        val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit { putBoolean(Constants.IS_ASSIST_SETUP_DONE, isSetupDone) }
    }

    fun updateDigitalAssistantPreferences(isAssistSetupDone: Boolean) {
        fragment.findPreference<androidx.preference.Preference>(Constants.DIGITAL_ASSISTANT_SETUP_PREF_KEY)?.isVisible =
            !isAssistSetupDone
        fragment.findPreference<androidx.preference.ListPreference>(Constants.DIGITAL_ASSISTANT_SELECT_PREF_KEY)
            ?.apply {
                isVisible = isAssistSetupDone
                isEnabled = isAssistSetupDone
            }
    }
}