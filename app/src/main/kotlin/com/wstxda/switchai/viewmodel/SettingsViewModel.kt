package com.wstxda.switchai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wstxda.switchai.R
import com.wstxda.switchai.data.ThemeMode
import com.wstxda.switchai.data.TopBarBlurStyle
import com.wstxda.switchai.repository.SettingsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import top.yukonga.miuix.kmp.theme.ThemeColorSpec
import top.yukonga.miuix.kmp.theme.ThemePaletteStyle

class SettingsViewModel(private val settings: SettingsRepository) : ViewModel() {

    val state = settings.state
    val savedState = settings.savedState
    private val messages = Channel<Int>(Channel.BUFFERED)
    val events = messages.receiveAsFlow()

    fun refreshSetup() = settings.refreshRole()

    fun selectAssistant(key: String) = write { settings.selectAssistant(key) }

    fun setTheme(value: ThemeMode) = write { settings.setTheme(value) }

    fun setMonet(enabled: Boolean) = write { settings.setMonet(enabled) }

    fun setKeyColor(index: Int) = write { settings.setKeyColor(index) }

    fun setPaletteStyle(value: ThemePaletteStyle) = write { settings.setPaletteStyle(value) }

    fun setColorSpec(value: ThemeColorSpec) = write { settings.setColorSpec(value) }

    fun setBlur(enabled: Boolean) = write { settings.setBlur(enabled) }

    fun setTopBarBlurStyle(value: TopBarBlurStyle) = write { settings.setTopBarBlurStyle(value) }

    fun setSelector(enabled: Boolean) = write { settings.setSelector(enabled) }

    fun setDynamicManager(enabled: Boolean) = write { settings.setDynamicManager(enabled) }

    fun setVoiceInput(enabled: Boolean) = write { settings.setVoiceInput(enabled) }

    fun setShizukuVoiceInput(enabled: Boolean) = write { settings.setShizukuVoiceInput(enabled) }

    fun setVibration(enabled: Boolean) = write { settings.setVibration(enabled) }

    fun setSound(enabled: Boolean) = write { settings.setSound(enabled) }

    fun setSelectorComponents(values: Set<String>) = write {
        settings.setSelectorComponents(values)
    }

    fun setVisibleAssistants(keys: Set<String>) = write { settings.setVisibleAssistants(keys) }

    fun setGrid(portrait: Int, landscape: Int) = write { settings.setGrid(portrait, landscape) }

    fun completeSetup() = write { settings.completeSetup() }

    fun dismissWarning() = write { settings.dismissWarning() }

    private fun write(block: suspend () -> Unit) {
        if (!savedState.value.loaded || savedState.value.loadError) return
        viewModelScope.launch {
            try {
                block()
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (_: Exception) {
                messages.send(R.string.settings_storage_error)
            }
        }
    }
}