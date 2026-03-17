package com.wstxda.switchai.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.wstxda.switchai.R
import com.wstxda.switchai.databinding.ActivityMainBinding
import com.wstxda.switchai.ui.component.AssistantTutorialBottomSheet

class MainActivity : BaseActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        applySystemBarInsets(binding.navHostContainer)

        setupToolbar(binding.toolbar, showBackButton = false)
        setupNavController()
    }

    override fun onDestroy() {
        super.onDestroy()
        navController.removeOnDestinationChangedListener(destinationChangedListener)
    }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener(destinationChangedListener)
    }

    private val destinationChangedListener =
        NavController.OnDestinationChangedListener { controller, destination, _ ->
            val label = destination.label?.toString() ?: getString(R.string.app_settings)
            binding.collapsingToolbar.title = label

            val isTopLevel = destination.id == controller.graph.startDestinationId
            supportActionBar?.setDisplayHomeAsUpEnabled(!isTopLevel)
            binding.toolbar.setNavigationOnClickListener {
                if (!isTopLevel) onBackPressedDispatcher.onBackPressed()
            }
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