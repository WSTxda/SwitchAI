package com.wstxda.switchai.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.wstxda.switchai.R
import com.wstxda.switchai.databinding.ActivityMainBinding
import com.wstxda.switchai.ui.component.AssistantTutorialBottomSheet

class MainActivity : BaseActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        applySystemBarInsets(binding.navHostContainer)

        setupToolbar(binding.toolbar, showBackButton = false)
        binding.collapsingToolbar.title = getString(R.string.app_settings)
    }

    override fun getMenuResId(): Int = R.menu.main_menu

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_show_tutorial -> {
                AssistantTutorialBottomSheet.show(supportFragmentManager)
                true
            }

            else -> false
        }
    }
}