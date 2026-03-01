package com.wstxda.switchai.logic

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import com.wstxda.switchai.ui.utils.SoundService.openAssistantSound
import com.wstxda.switchai.ui.utils.VibrationService.openAssistantVibration
import com.wstxda.switchai.utils.AssistantProperties
import kotlin.reflect.full.companionObjectInstance

fun Context.openAssistant(
    intents: List<Intent>,
    errorMessage: Int,
): Boolean {
    intents.forEach { intent ->
        if (openAssistant(intent)) {
            openAssistantVibration()
            openAssistantSound()
            return true
        }
    }

    val pkg = this::class.companionObjectInstance.let { it as? AssistantProperties }?.packageName
    val handled = pkg?.takeIf { it.isNotEmpty() }?.let { openOnStore(it) } ?: false

    showToast(errorMessage)
    return handled
}

fun Context.openAssistant(intent: Intent): Boolean = runCatching {
    startActivity(Intent(intent).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK })
    true
}.getOrElse { false }

fun Context.openAssistantRoot(
    intents: List<Intent>,
    rootAccessMessage: Int,
    errorMessage: Int,
): Boolean {
    val hasRoot = runCatching { isRootAvailable() }.getOrElse { false }

    if (!hasRoot) {
        showToast(rootAccessMessage)
        return false
    }

    intents.forEach { intent ->
        val component = intent.component ?: return@forEach
        val success = runCatching {
            launchRootActivity(component.packageName, component.className)
        }.getOrElse { false }

        if (success) {
            openAssistantVibration()
            openAssistantSound()
            return true
        }
    }

    val pkg = this::class.companionObjectInstance.let { it as? AssistantProperties }?.packageName
    val handled = pkg?.takeIf { it.isNotEmpty() }?.let { openOnStore(it) } ?: false

    showToast(errorMessage)
    return handled
}

fun Context.openOnStore(packageName: String): Boolean {
    val marketIntent = Intent(
        Intent.ACTION_VIEW, "market://details?id=$packageName".toUri()
    ).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }

    val webSearchIntent = Intent(
        Intent.ACTION_VIEW, "https://www.google.com/search?q=$packageName+android+app".toUri()
    ).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }

    return runCatching {
        startActivity(marketIntent)
        true
    }.getOrElse {
        runCatching {
            startActivity(webSearchIntent)
            true
        }.getOrElse { false }
    }
}

fun Context.showToast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}