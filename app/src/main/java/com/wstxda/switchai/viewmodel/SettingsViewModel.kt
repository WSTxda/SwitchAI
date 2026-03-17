package com.wstxda.switchai.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wstxda.switchai.ui.ThemeManager

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val _isAssistSetupDone = MutableLiveData<Boolean>()
    val isAssistSetupDone: LiveData<Boolean> = _isAssistSetupDone

    fun setAssistSetupDone(done: Boolean) {
        _isAssistSetupDone.value = done
    }

    fun applyTheme(themeKey: String) {
        ThemeManager.applyTheme(themeKey)
    }
}