package com.wstxda.switchai.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.net.toUri
import com.wstxda.switchai.constants.AssistantsMap

internal fun Context.openPreferenceUrl(url: String, onFailure: () -> Unit) {
    startPreferenceActivity(Intent(Intent.ACTION_VIEW, url.toUri()), onFailure)
}

internal fun Context.openAppInfo(onFailure: () -> Unit) {
    startPreferenceActivity(
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, "package:$packageName".toUri()),
        onFailure,
    )
}

internal fun Context.openSupportedAssistant(key: String, onFailure: () -> Unit) {
    runCatching {
        startActivity(
            Intent(
                this,
                AssistantsMap.assistantActivity.getValue(key),
            )
        )
    }.onFailure { onFailure() }
}

private fun Context.startPreferenceActivity(intent: Intent, onFailure: () -> Unit) {
    runCatching { startActivity(intent) }.onFailure { onFailure() }
}

fun Context.openOnStore(packageName: String): Boolean {
    val marketIntent = Intent(
        Intent.ACTION_VIEW,
        "market://details?id=$packageName".toUri(),
    ).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }

    val webSearchIntent = Intent(
        Intent.ACTION_VIEW,
        "https://www.google.com/search?q=$packageName+android+app".toUri(),
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

internal fun Context.tryStartActivity(intent: Intent): Boolean = runCatching {
    startActivity(intent)
    true
}.getOrDefault(false)