package com.wstxda.switchai.fragment.preferences

import android.os.Bundle
import androidx.preference.Preference
import com.wstxda.switchai.R
import com.wstxda.switchai.ui.TileManager
import com.wstxda.switchai.ui.WidgetManager
import com.wstxda.switchai.utils.Constants

class ShortcutsPreferencesFragment : BasePreferencesFragment() {

    private val widgetManager by lazy { WidgetManager(requireContext()) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences_shortcuts, rootKey)
        setupTilePreference()
        setupWidgetPreference()
    }

    private fun setupTilePreference() {
        findPreference<Preference>(Constants.SHORTCUT_TILE_PREF_KEY)?.setOnPreferenceClickListener {
            TileManager(requireContext()).requestAddTile()
            true
        }
    }

    private fun setupWidgetPreference() {
        findPreference<Preference>(Constants.SHORTCUT_WIDGET_PREF_KEY)?.setOnPreferenceClickListener {
            widgetManager.requestAddAssistantWidget()
            true
        }
    }
}