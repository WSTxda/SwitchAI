package com.wstxda.switchai.activity

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.wstxda.switchai.R
import com.wstxda.switchai.data.SettingsState
import com.wstxda.switchai.switchAI
import com.wstxda.switchai.utils.showToast
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

abstract class AssistantActivity : BaseActivity() {

    lateinit var launchSettings: SettingsState
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            finish()
            return
        }
        lifecycleScope.launch {
            try {
                launchSettings = switchAI.settings.awaitSavedSettings()
                onCreateInternal()
            } catch (error: CancellationException) {
                throw error
            } catch (_: IOException) {
                showToast(R.string.settings_storage_error)
            } catch (_: Exception) {
                showToast(R.string.assistant_open_error)
            } finally {
                finish()
            }
        }
    }

    abstract fun onCreateInternal()

    fun createAssistantIntent(
        packageName: String,
        voiceInputActivity: String,
        defaultActivity: String,
    ): Intent = Intent().apply {
        component = ComponentName(
            packageName,
            if (launchSettings.voiceInput) voiceInputActivity else defaultActivity,
        )
    }
}