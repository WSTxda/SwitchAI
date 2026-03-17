package com.wstxda.switchai.fragment.preferences

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.preference.ListPreference
import com.wstxda.switchai.R
import com.wstxda.switchai.utils.Constants
import com.wstxda.switchai.viewmodel.SettingsViewModel

class AppearancePreferencesFragment : BasePreferencesFragment() {

    private val settingsViewModel: SettingsViewModel by activityViewModels()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_appearance, rootKey)
        setupThemePreference()
    }

    private fun setupThemePreference() {
        findPreference<ListPreference>(Constants.THEME_PREF_KEY)?.setOnPreferenceChangeListener { _, newValue ->
            settingsViewModel.applyTheme(newValue.toString())
            true
        }
    }
}