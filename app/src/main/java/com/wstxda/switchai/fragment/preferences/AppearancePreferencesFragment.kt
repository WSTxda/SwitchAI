package com.wstxda.switchai.fragment.preferences

import androidx.fragment.app.activityViewModels
import androidx.preference.ListPreference
import com.wstxda.switchai.R
import com.wstxda.switchai.fragment.BasePreferenceFragment
import com.wstxda.switchai.utils.Constants
import com.wstxda.switchai.viewmodel.SettingsViewModel

class AppearancePreferencesFragment : BasePreferenceFragment() {

    private val settingsViewModel: SettingsViewModel by activityViewModels()

    override val preferencesResId: Int get() = R.xml.preferences_appearance

    override fun setupListeners() {
        findPreference<ListPreference>(Constants.THEME_PREF_KEY)?.setOnPreferenceChangeListener { _, newValue ->
            settingsViewModel.applyTheme(newValue.toString())
            true
        }
    }
}