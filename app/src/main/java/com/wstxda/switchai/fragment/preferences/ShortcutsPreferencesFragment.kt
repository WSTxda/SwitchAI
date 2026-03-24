package com.wstxda.switchai.fragment.preferences

import androidx.preference.Preference
import com.wstxda.switchai.R
import com.wstxda.switchai.fragment.BasePreferenceFragment
import com.wstxda.switchai.ui.TileManager
import com.wstxda.switchai.ui.WidgetManager
import com.wstxda.switchai.utils.Constants

class ShortcutsPreferencesFragment : BasePreferenceFragment() {

    private val widgetManager by lazy { WidgetManager(requireContext()) }

    override val preferencesResId: Int get() = R.xml.preferences_shortcuts

    override fun setupListeners() {
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