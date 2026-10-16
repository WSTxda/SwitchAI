package com.wstxda.switchai.logic

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.activity.ShizukuSetupActivity
import com.wstxda.switchai.data.ShizukuLaunchResult
import com.wstxda.switchai.data.ShizukuState
import com.wstxda.switchai.ui.utils.SoundService.openAssistantSound
import com.wstxda.switchai.ui.utils.VibrationService.openAssistantVibration
import com.wstxda.switchai.utils.AssistantProperties
import kotlin.reflect.full.companionObjectInstance

fun Context.onAssistantOpened() {
    openAssistantVibration()
    openAssistantSound()
}

fun Context.openAssistant(
    intents: List<Intent>,
    errorMessage: Int,
): Boolean {
    intents.forEach { intent ->
        if (openAssistant(intent)) {
            onAssistantOpened()
            return true
        }
    }

    val pkg = intents.firstNotNullOfOrNull { intent ->
        (intent.component?.packageName ?: intent.`package`)?.takeIf { it.isNotBlank() }
    } ?: runCatching {
        (this::class.companionObjectInstance as? AssistantProperties)?.packageName?.takeIf { it.isNotBlank() }
    }.getOrNull()

    val handled = pkg?.let { openOnStore(it) } ?: false

    showToast(errorMessage)
    return handled
}

fun Context.openAssistant(intent: Intent): Boolean = runCatching {
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
    true
}.getOrElse { false }

fun Context.openAssistantShizuku(
    intent: Intent,
    errorMessage: Int,
): Boolean {
    if (!PreferenceHelper(this).isShizukuVoiceInputEnabled()) return false

    val component = intent.component ?: run {
        showToast(errorMessage)
        return false
    }

    val isInstalled = runCatching {
        packageManager.getApplicationInfo(component.packageName, 0)
    }.isSuccess

    if (!isInstalled) {
        showToast(errorMessage)
        return openOnStore(component.packageName)
    }

    fun openSetup(): Boolean = runCatching {
        startActivity(
            ShizukuSetupActivity.createIntent(this, component, errorMessage)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
        true
    }.getOrElse {
        showToast(errorMessage)
        false
    }

    if (this is AssistantActivity && ShizukuChecker.getState(this) == ShizukuState.READY) {
        openAndFinish {
            when (ShizukuLauncher.launch(this, component)) {
                ShizukuLaunchResult.SUCCESS -> onAssistantOpened()
                ShizukuLaunchResult.PERMISSION_REQUIRED, ShizukuLaunchResult.UNAVAILABLE -> openSetup()

                ShizukuLaunchResult.TARGET_FAILED -> showToast(errorMessage)
            }
        }
        return true
    }

    return openSetup()
}

fun Context.openOnStore(packageName: String): Boolean {
    if (packageName.isBlank()) return false

    val marketIntent = Intent(
        Intent.ACTION_VIEW, "market://details?id=$packageName".toUri()
    )

    val webSearchIntent = Intent(
        Intent.ACTION_VIEW,
        "https://www.google.com/search".toUri().buildUpon()
            .appendQueryParameter("q", "$packageName android app").build()
    )

    return openAssistant(marketIntent) || openAssistant(webSearchIntent)
}

fun Context.showToast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}