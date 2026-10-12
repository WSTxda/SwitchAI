package com.wstxda.switchai.activity

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.wstxda.switchai.databinding.ActivityMainBinding
import com.wstxda.switchai.service.UpdaterService
import com.wstxda.switchai.ui.component.FreeAndroidWarnDialog

class MainActivity : BaseActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        FreeAndroidWarnDialog.show(supportFragmentManager, this)
        UpdaterService.checkForUpdatesAuto(lifecycleScope, this, supportFragmentManager)
    }
}