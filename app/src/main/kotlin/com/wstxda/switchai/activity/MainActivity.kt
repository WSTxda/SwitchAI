package com.wstxda.switchai.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.wstxda.switchai.switchAI
import com.wstxda.switchai.ui.SwitchAIApp
import com.wstxda.switchai.viewmodel.SettingsViewModel

class MainActivity : BaseActivity() {

    private val settings: SettingsViewModel by viewModels { switchAI.settingsViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)
        splash.setKeepOnScreenCondition { !settings.state.value.loaded }
        setContent { SwitchAIApp(settings) }
    }
}