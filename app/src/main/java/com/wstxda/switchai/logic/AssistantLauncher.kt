package com.wstxda.switchai.logic

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri

fun Context.launchAssistant(
    intents: List<Intent>,
    errorMessageResId: Int,
    packageName: String? = null,
): Boolean {
    intents.forEach { intent ->
        if (launchAssistant(intent)) return true
    }
    var handled = false
    if (!packageName.isNullOrEmpty()) {
        handled = launchOnStore(packageName)
    }
    showToast(errorMessageResId)
    return handled
}

fun Context.launchAssistant(intent: Intent): Boolean = runCatching {
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
    true
}.getOrElse { false }

fun Context.launchAssistantRoot(
    intents: List<Intent>,
    rootAccessMessageResId: Int,
    errorMessageResId: Int,
    packageName: String? = null
): Boolean {
    val hasRoot = runCatching {
        RootChecker.isRootAvailable()
    }.getOrElse { false }

    if (!hasRoot) {
        showToast(rootAccessMessageResId)
        return false
    }

    intents.forEach { intent ->
        val success = runCatching {
            RootChecker.launchRootActivity(
                intent.component!!.packageName,
                intent.component!!.className
            )
        }.getOrElse { false }

        if (success) return true
    }

    val handled = !packageName.isNullOrEmpty() && launchOnStore(packageName)
    showToast(errorMessageResId)
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

fun Context.showToast(messageResId: Int) {
    Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
}