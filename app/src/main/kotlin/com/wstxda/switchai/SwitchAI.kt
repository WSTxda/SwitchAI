package com.wstxda.switchai

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.service.quicksettings.TileService
import android.util.Log
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.wstxda.switchai.logic.PackageChecker
import com.wstxda.switchai.repository.SettingsRepository
import com.wstxda.switchai.service.AssistantTileService
import com.wstxda.switchai.ui.utils.updateDynamicShortcuts
import com.wstxda.switchai.viewmodel.SettingsViewModel
import com.wstxda.switchai.widget.requestMaterialWidgetUpdates
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import rikka.shizuku.ShizukuProvider

class SwitchAI : Application() {
    init {
        ShizukuProvider.disableAutomaticSuiInitialization()
    }

    val applicationScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            Log.e("SwitchAI", "Background operation failed", error)
        })
    lateinit var settings: SettingsRepository
        private set
    val packageChecker by lazy { PackageChecker(this, applicationScope) }

    val settingsViewModelFactory = viewModelFactory { initializer { SettingsViewModel(settings) } }

    override fun onCreate() {
        super.onCreate()
        settings = SettingsRepository(this, applicationScope)
        applicationScope.launch {
            settings.savedState.filter { it.loaded && !it.loadError }.map { it.assistant }
                .distinctUntilChanged().collect { assistant ->
                    updateDynamicShortcuts(assistant)
                    requestMaterialWidgetUpdates()
                }
        }
        applicationScope.launch {
            settings.state.filter { it.loaded && !it.loadError }
                .map { Triple(it.assistant, it.selectorEnabled, it.setupDone) }
                .distinctUntilChanged().collect {
                    try {
                        TileService.requestListeningState(
                            this@SwitchAI,
                            ComponentName(this@SwitchAI, AssistantTileService::class.java),
                        )
                    } catch (error: SecurityException) {
                        Log.w("SwitchAI", "Unable to request tile refresh for this user", error)
                    } catch (error: IllegalArgumentException) {
                        Log.w("SwitchAI", "Tile refresh requested outside current user", error)
                    }
                }
        }
    }
}

val Context.switchAI: SwitchAI
    get() = applicationContext as SwitchAI