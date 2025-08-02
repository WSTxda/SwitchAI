package com.wstxda.switchai.logic

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import com.wstxda.switchai.ui.utils.VibrationService.openAssistantVibration
import com.wstxda.switchai.utils.AssistantProperties
import kotlin.reflect.full.companionObjectInstance

fun Context.launchAssistant(
    intents: List<Intent>,
    errorMessage: Int,
): Boolean {
    intents.forEach { intent ->
        if (launchAssistant(intent)) return true
    }

    val pkg = this::class.companionObjectInstance.let { it as? AssistantProperties }?.packageName
    val handled = pkg?.takeIf { it.isNotEmpty() }?.let { launchOnStore(it) } ?: false

    showToast(errorMessage)
    return handled
}

fun Context.launchAssistant(intent: Intent): Boolean = runCatching {
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    openAssistantVibration()
    startActivity(intent)
    true
}.getOrElse { false }

fun Context.launchAssistantRoot(
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
            return true
        }
    }

    val pkg = this::class.companionObjectInstance.let { it as? AssistantProperties }?.packageName
    val handled = pkg?.takeIf { it.isNotEmpty() }?.let { launchOnStore(it) } ?: false

    showToast(errorMessage)
    return handled
}

fun Context.launchOnStore(packageName: String): Boolean {
    val uri = "market://details?id=$packageName".toUri()
    val goToMarket = Intent(Intent.ACTION_VIEW, uri).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    val fallbackUri = "https://play.google.com/store/apps/details?id=$packageName".toUri()
    val fallbackIntent = Intent(Intent.ACTION_VIEW, fallbackUri).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    return try {
        startActivity(goToMarket)
        true
    } catch (_: ActivityNotFoundException) {
        runCatching {
            startActivity(fallbackIntent)
            true
        }.getOrElse { false }
    }
}

fun Context.showToast(message: Int) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}