package com.wstxda.switchai.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.wstxda.switchai.R
import com.wstxda.switchai.data.ShizukuLaunchResult
import com.wstxda.switchai.data.ShizukuState
import com.wstxda.switchai.databinding.ActivityShizukuSetupBinding
import com.wstxda.switchai.fragment.ShizukuSetupFragment
import com.wstxda.switchai.logic.ShizukuChecker
import com.wstxda.switchai.logic.ShizukuLauncher
import com.wstxda.switchai.logic.onAssistantOpened
import com.wstxda.switchai.logic.showToast
import com.wstxda.switchai.utils.Constants
import kotlinx.coroutines.launch
import rikka.shizuku.Shizuku

class ShizukuSetupActivity : BaseActivity() {

    companion object {
        fun createIntent(
            context: Context, component: ComponentName, errorMessageResId: Int
        ): Intent = Intent(context, ShizukuSetupActivity::class.java).apply {
            putExtra(
                Constants.EXTRA_SHIZUKU_TARGET_COMPONENT, component.flattenToString()
            )
            putExtra(Constants.EXTRA_SHIZUKU_ERROR_MESSAGE, errorMessageResId)
        }
    }

    private lateinit var binding: ActivityShizukuSetupBinding
    private var targetComponent: ComponentName? = null

    private val setupOnly by lazy {
        intent.getBooleanExtra(Constants.EXTRA_SHIZUKU_SETUP_ONLY, false)
    }

    private var errorMessageResId = R.string.assistant_application_not_found
    private var setupShown = false
    private var launchStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val component = ComponentName.unflattenFromString(
            intent.getStringExtra(Constants.EXTRA_SHIZUKU_TARGET_COMPONENT).orEmpty()
        )

        if (component == null && !setupOnly) {
            finish()
            return
        }

        targetComponent = component
        errorMessageResId = intent.getIntExtra(
            Constants.EXTRA_SHIZUKU_ERROR_MESSAGE, R.string.assistant_application_not_found
        )

        val launchInterrupted =
            savedInstanceState?.getBoolean(Constants.STATE_SHIZUKU_LAUNCH_STARTED) == true

        setupShown =
            savedInstanceState?.getBoolean(Constants.STATE_SHIZUKU_SETUP_SHOWN) == true || launchInterrupted

        if (!setupOnly && !setupShown && ShizukuChecker.getState(this) == ShizukuState.READY) {
            launchTarget()
            return
        }

        showSetup()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(Constants.STATE_SHIZUKU_SETUP_SHOWN, setupShown)
        outState.putBoolean(Constants.STATE_SHIZUKU_LAUNCH_STARTED, launchStarted)
        super.onSaveInstanceState(outState)
    }

    private fun showSetup(permissionInvalidated: Boolean = false) {
        if (!::binding.isInitialized) {
            binding = ActivityShizukuSetupBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setupShown = true

            setupToolbar(binding.toolbar)
            binding.collapsingToolbar.title = getString(R.string.app_shizuku_setup)
        }

        val containerId = binding.shizukuSetupFragmentContainer.id

        if (supportFragmentManager.findFragmentById(containerId) == null) {
            supportFragmentManager.beginTransaction().replace(
                containerId, ShizukuSetupFragment.newInstance(
                    component = targetComponent,
                    errorMessageResId = errorMessageResId,
                    permissionInvalidated = permissionInvalidated,
                    setupOnly = setupOnly
                )
            ).commit()
        }
    }

    private fun launchTarget() {
        if (setupOnly || launchStarted) return

        val component = targetComponent ?: return
        launchStarted = true

        lifecycleScope.launch {
            when (ShizukuLauncher.launch(this@ShizukuSetupActivity, component)) {
                ShizukuLaunchResult.SUCCESS -> {
                    onAssistantOpened()
                    finish()
                }

                ShizukuLaunchResult.PERMISSION_REQUIRED -> {
                    launchStarted = false
                    showSetup(permissionInvalidated = true)
                }

                ShizukuLaunchResult.UNAVAILABLE -> {
                    launchStarted = false
                    showSetup(permissionInvalidated = Shizuku.pingBinder())
                }

                ShizukuLaunchResult.TARGET_FAILED -> {
                    launchStarted = false
                    showToast(errorMessageResId)
                    finish()
                }
            }
        }
    }
}