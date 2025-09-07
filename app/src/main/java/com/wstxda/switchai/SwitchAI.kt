package com.wstxda.switchai

import android.app.Application
import android.content.SharedPreferences
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.ui.ShortcutManager
import com.wstxda.switchai.ui.ThemeManager
import com.wstxda.switchai.utils.Constants

class SwitchAI : Application(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val prefs by lazy { PreferenceHelper(this) }
    private val shortcuts by lazy { ShortcutManager(this) }

    override fun onCreate() {
        super.onCreate()
        ThemeManager.applyTheme(
            prefs.getString(Constants.THEME_PREF_KEY, Constants.THEME_SYSTEM)
                ?: Constants.THEME_SYSTEM
        )

        shortcuts.updateShortcuts()
        prefs.registerListener(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        prefs.unregisterListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == Constants.DIGITAL_ASSISTANT_SELECT_PREF_KEY) shortcuts.updateShortcuts()
    }
}