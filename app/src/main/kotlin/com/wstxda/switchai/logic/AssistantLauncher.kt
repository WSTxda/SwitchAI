package com.wstxda.switchai.logic

import android.content.Context
import android.content.Intent
import com.wstxda.switchai.activity.AssistantActivity
import com.wstxda.switchai.activity.ShizukuSetupActivity
import com.wstxda.switchai.constants.AssistantsMap
import com.wstxda.switchai.utils.openAssistantSound
import com.wstxda.switchai.utils.openAssistantVibration
import com.wstxda.switchai.utils.openOnStore
import com.wstxda.switchai.utils.showToast

fun Context.openAssistant(intents: List<Intent>, errorMessage: Int, packageName: String): Boolean {
    intents.forEach { intent ->
        if (openAssistant(intent)) {
            (this as? AssistantActivity)?.launchSettings?.let {
                openAssistantVibration(it.vibration)
                openAssistantSound(it.sound)
            }
            return true
        }
    }
    val handled = packageName.takeIf(String::isNotEmpty)?.let { openOnStore(it) } ?: false
    showToast(errorMessage)
    return handled
}

fun Context.openAssistant(intent: Intent): Boolean = runCatching {
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
    true
}.getOrDefault(false)

fun Context.openAssistantShizuku(intents: List<Intent>, errorMessage: Int): Boolean {
    val component = intents.firstOrNull()?.component ?: return false
    startActivity(
        ShizukuSetupActivity.createIntent(this, component, errorMessage).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        })
    return true
}

internal fun Context.createEntryIntent(key: String): Intent? =
    AssistantsMap.assistantActivity[key]?.let { Intent(this, it) }

internal fun Context.launchAssistantEntry(key: String) {
    val intent = createEntryIntent(key) ?: return
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivity(intent)
}