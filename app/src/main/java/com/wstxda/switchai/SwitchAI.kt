package com.wstxda.switchai

import android.app.Application
import com.wstxda.switchai.logic.PreferenceHelper
import com.wstxda.switchai.ui.ThemeManager
import com.wstxda.switchai.utils.Constants

class SwitchAI : Application() {
    override fun onCreate() {
        super.onCreate()
        val preferenceHelper = PreferenceHelper(this)
        val selectedTheme = preferenceHelper.getString(Constants.THEME_PREF_KEY, Constants.THEME_SYSTEM)
        selectedTheme?.let { ThemeManager.applyTheme(it) }
    }
}