package com.wstxda.switchai.activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.wstxda.switchai.R
import com.wstxda.switchai.constants.Constants
import com.wstxda.switchai.data.ShizukuLaunchResult
import com.wstxda.switchai.data.ShizukuSetupState
import com.wstxda.switchai.data.ShizukuState
import com.wstxda.switchai.logic.ShizukuChecker
import com.wstxda.switchai.logic.ShizukuLauncher
import com.wstxda.switchai.switchAI
import com.wstxda.switchai.ui.screens.ShizukuSetupScreen
import com.wstxda.switchai.ui.theme.SwitchAITheme
import com.wstxda.switchai.utils.openAssistantSound
import com.wstxda.switchai.utils.openAssistantVibration
import com.wstxda.switchai.utils.showToast
import com.wstxda.switchai.utils.tryStartActivity
import com.wstxda.switchai.viewmodel.SettingsViewModel
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import rikka.shizuku.Shizuku

class ShizukuSetupActivity : BaseActivity() {

    companion object {
        fun createIntent(context: Context, component: ComponentName, errorMessageResId: Int) =
            Intent(context, ShizukuSetupActivity::class.java).apply {
                putExtra(Constants.EXTRA_SHIZUKU_TARGET_COMPONENT, component.flattenToString())
                putExtra(Constants.EXTRA_SHIZUKU_ERROR_MESSAGE, errorMessageResId)
            }
    }

    private val settings: SettingsViewModel by viewModels { switchAI.settingsViewModelFactory }
    private lateinit var targetComponent: ComponentName
    private var errorMessageResId = R.string.assistant_application_not_found
    private var setupShown = false
    private var setupContentShown = false
    private var launchStarted = false
    private var permissionInvalidated = false
    private var binderDied = false
    private var observing = false
    private var state by mutableStateOf(ShizukuSetupState())
    private val binderReceivedListener = Shizuku.OnBinderReceivedListener {
        runOnUiThread {
            if (!observing || isDestroyed) return@runOnUiThread
            val access = ShizukuChecker.getState(this)
            if (binderDied && access == ShizukuState.READY) permissionInvalidated = false
            binderDied = false
            refreshState(access)
        }
    }
    private val binderDeadListener = Shizuku.OnBinderDeadListener {
        runOnUiThread {
            if (!observing || isDestroyed) return@runOnUiThread
            binderDied = true
            refreshState()
        }
    }
    private val permissionResultListener =
        Shizuku.OnRequestPermissionResultListener { code, result ->
            if (code == Constants.SHIZUKU_PERMISSION_REQUEST_CODE) runOnUiThread {
                if (!observing || isDestroyed) return@runOnUiThread
                permissionInvalidated = result != PackageManager.PERMISSION_GRANTED
                state = state.copy(permissionPending = false)
                refreshState(
                    if (permissionInvalidated) ShizukuState.PERMISSION_REQUIRED
                    else ShizukuState.READY
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val component = ComponentName.unflattenFromString(
            intent.getStringExtra(Constants.EXTRA_SHIZUKU_TARGET_COMPONENT).orEmpty()
        )
        if (component == null) {
            finish()
            return
        }
        targetComponent = component
        errorMessageResId = intent.getIntExtra(
            Constants.EXTRA_SHIZUKU_ERROR_MESSAGE,
            R.string.assistant_application_not_found,
        )
        setupShown =
            savedInstanceState?.getBoolean(Constants.STATE_SHIZUKU_SETUP_SHOWN) == true || savedInstanceState?.getBoolean(
                Constants.STATE_SHIZUKU_LAUNCH_STARTED
            ) == true
        permissionInvalidated =
            savedInstanceState?.getBoolean(Constants.STATE_SHIZUKU_PERMISSION_INVALIDATED) ?: false
        if (!setupShown && ShizukuChecker.getState(this) == ShizukuState.READY) {
            launchTarget()
            return
        }
        showSetup(permissionInvalidated)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(Constants.STATE_SHIZUKU_SETUP_SHOWN, setupShown)
        outState.putBoolean(Constants.STATE_SHIZUKU_LAUNCH_STARTED, launchStarted)
        outState.putBoolean(Constants.STATE_SHIZUKU_PERMISSION_INVALIDATED, permissionInvalidated)
        super.onSaveInstanceState(outState)
    }

    private fun showSetup(invalidated: Boolean = false) {
        permissionInvalidated = invalidated
        setupShown = true
        if (!setupContentShown) {
            setupContentShown = true
            setContent {
                val preferences by settings.state.collectAsStateWithLifecycle()
                SwitchAITheme(preferences.appearance) {
                    ShizukuSetupScreen(
                        state = state,
                        loading = !preferences.loaded || state.access == null,
                        storageError = preferences.loadError,
                        onBack = ::finish,
                        onManager = {
                            if (state.access == ShizukuState.NOT_INSTALLED) openDownload()
                            else openManager()
                        },
                        onPermission = ::requestPermission,
                        onContinue = ::launchTarget,
                    )
                }
            }
        }
        refreshState()
    }

    private fun refreshState(access: ShizukuState = ShizukuChecker.getState(this)) {
        val effective =
            if (permissionInvalidated && access == ShizukuState.READY) ShizukuState.PERMISSION_REQUIRED
            else access
        state = state.copy(access = effective, launching = launchStarted)
    }

    private fun requestPermission() {
        if (state.permissionPending) return
        refreshState()
        if (state.access != ShizukuState.PERMISSION_REQUIRED) return
        runCatching {
            state = state.copy(permissionPending = true)
            Shizuku.requestPermission(Constants.SHIZUKU_PERMISSION_REQUEST_CODE)
        }.onFailure {
            state = state.copy(permissionPending = false)
            refreshState()
        }
    }

    private fun launchTarget() {
        if (launchStarted) return
        if (setupShown) {
            refreshState()
            if (state.access != ShizukuState.READY) return
        }
        launchStarted = true
        state = state.copy(launching = true)
        lifecycleScope.launch {
            try {
                val preferences = switchAI.settings.awaitSavedSettings()
                when (ShizukuLauncher.launch(this@ShizukuSetupActivity, targetComponent)) {
                    ShizukuLaunchResult.SUCCESS -> {
                        openAssistantVibration(preferences.vibration)
                        openAssistantSound(preferences.sound)
                        finish()
                    }

                    ShizukuLaunchResult.PERMISSION_REQUIRED -> {
                        launchStarted = false
                        showSetup(invalidated = true)
                    }

                    ShizukuLaunchResult.UNAVAILABLE -> {
                        launchStarted = false
                        showSetup(invalidated = Shizuku.pingBinder())
                    }

                    ShizukuLaunchResult.TARGET_FAILED -> {
                        launchStarted = false
                        showToast(errorMessageResId)
                        finish()
                    }
                }
            } catch (error: CancellationException) {
                throw error
            } catch (_: IOException) {
                showToast(R.string.settings_storage_error)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (!::targetComponent.isInitialized) return
        Shizuku.addBinderReceivedListener(binderReceivedListener)
        Shizuku.addBinderDeadListener(binderDeadListener)
        Shizuku.addRequestPermissionResultListener(permissionResultListener)
        observing = true
    }

    override fun onResume() {
        super.onResume()
        if (::targetComponent.isInitialized) refreshState()
    }

    override fun onStop() {
        if (observing) {
            Shizuku.removeBinderReceivedListener(binderReceivedListener)
            Shizuku.removeBinderDeadListener(binderDeadListener)
            Shizuku.removeRequestPermissionResultListener(permissionResultListener)
            observing = false
        }
        state = state.copy(permissionPending = false)
        super.onStop()
    }

    private fun openManager() {
        val intent = packageManager.getLaunchIntentForPackage(Constants.SHIZUKU_MANAGER_PACKAGE)
        if (intent == null || !tryStartActivity(intent.apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })) openDownload()
    }

    private fun openDownload() {
        if (!tryStartActivity(
                Intent(Intent.ACTION_VIEW, Constants.SHIZUKU_DOWNLOAD_URL.toUri()).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
        ) showToast(R.string.assistant_open_error)
    }
}