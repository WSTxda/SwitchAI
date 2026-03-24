package com.wstxda.switchai.fragment.preferences

import android.content.Intent
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import com.wstxda.switchai.R
import com.wstxda.switchai.activity.LibraryActivity
import com.wstxda.switchai.fragment.BasePreferenceFragment
import com.wstxda.switchai.utils.Constants
import com.wstxda.switchai.viewmodel.AboutViewModel

class AboutPreferencesFragment : BasePreferenceFragment() {

    private val aboutViewModel: AboutViewModel by viewModels()

    override val preferencesResId: Int get() = R.xml.preferences_about

    override fun setupListeners() {
        setupLibraryPreference()
        setupLinkPreferences()
    }

    private fun setupLibraryPreference() {
        findPreference<Preference>(Constants.LIBRARY_PREF_KEY)?.setOnPreferenceClickListener {
            startActivity(Intent(requireContext(), LibraryActivity::class.java))
            true
        }
    }

    private fun setupLinkPreferences() {
        aboutViewModel.links.forEach { (key, url) ->
            findPreference<Preference>(key)?.setOnPreferenceClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, url.toUri()))
                true
            }
        }
    }
}