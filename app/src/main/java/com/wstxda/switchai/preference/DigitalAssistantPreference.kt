package com.wstxda.switchai.preference

import android.content.Context
import androidx.core.content.edit
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.wstxda.switchai.logic.DigitalAssistantChecker
import com.wstxda.switchai.utils.Constants

class DigitalAssistantPreference(private val fragment: PreferenceFragmentCompat) {

    private val context: Context get() = fragment.requireContext()

    fun checkDigitalAssistSetupStatus(): Boolean = DigitalAssistantChecker.isSetupDone(context)

    fun setDigitalAssistSetupStatus(isSetupDone: Boolean) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs.edit { putBoolean(Constants.IS_ASSIST_SETUP_DONE, isSetupDone) }
    }

    fun updateDigitalAssistantPreferences(isAssistSetupDone: Boolean) {
        fragment.findPreference<Preference>(Constants.DIGITAL_ASSISTANT_SETUP_PREF_KEY)?.isVisible =
            !isAssistSetupDone
        fragment.findPreference<ListPreference>(Constants.DIGITAL_ASSISTANT_SELECT_PREF_KEY)
            ?.apply {
                isVisible = isAssistSetupDone
                isEnabled = isAssistSetupDone
            }
    }
}