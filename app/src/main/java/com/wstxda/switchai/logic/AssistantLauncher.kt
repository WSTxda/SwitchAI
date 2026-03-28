package com.wstxda.switchai.logic

import android.content.ActivityNotFoundException
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
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
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
        val success = runCatching {
            launchRootActivity(
                intent.component!!.packageName, intent.component!!.className
            )
        }.getOrElse { false }

        if (success) {
            openAssistantVibration()
            openAssistantSound()
            return true
        }
    }

    val pkg = this::class.companionObjectInstance.let { it as? AssistantProperties }?.packageName

    val isInstalled = pkg?.let {
        runCatching { packageManager.getPackageInfo(it, 0); true }.getOrElse { false }
    } ?: false

    return if (isInstalled) {
        openAssistant(intents, errorMessage)
    } else {
        showToast(errorMessage)
        pkg?.takeIf { it.isNotEmpty() }?.let { openOnStore(it) } ?: false
    }
}

fun Context.openOnStore(packageName: String): Boolean {
    val marketIntent = Intent(
        Intent.ACTION_VIEW, "market://details?id=$packageName".toUri()
    ).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }

    val webSearchIntent = Intent(
        Intent.ACTION_VIEW, "https://www.google.com/search?q=$packageName+android+app".toUri()
    ).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }

    return try {
        startActivity(marketIntent)
        true
    } catch (_: ActivityNotFoundException) {
        runCatching {
            startActivity(webSearchIntent)
            true
        }.getOrElse { false }
    }
}

fun Context.showToast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}